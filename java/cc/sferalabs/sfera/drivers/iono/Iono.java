package cc.sferalabs.sfera.drivers.iono;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.drivers.Driver;
import cc.sferalabs.sfera.events.Bus;

public class Iono extends Driver {

	private String ionoId;
	private InetAddress ipAddress;
	private int port;

	private JSONParser jsonParser = new JSONParser();
	private DatagramSocket monitorSocket;
	int lastPr = -1;

	public Iono(String id) {
		super(id);
	}

	@Override
	public boolean onInit(Configuration configuration)
			throws InterruptedException {
		String ip = configuration.getProperty("ip", null);
		if (ip == null) {
			log.error("no IP address specified");
			return false;
		}
		try {
			ipAddress = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			log.error(e.toString());
			return false;
		}
		port = configuration.getIntProperty("port", 7878);

		try {
			monitorSocket = new DatagramSocket(port);
			monitorSocket.setSoTimeout(60000);
		} catch (SocketException e) {
			log.error("error initializing monitor socket: " + e);
			return false;
		}

		try {
			getState();
		} catch (Exception e) {
			log.error("initialization error: " + e);
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void getState() throws Exception {
		try (DatagramSocket socket = new DatagramSocket()) {
			DatagramPacket replyPacket = send("state");

			JSONObject obj = getJsonObject(replyPacket);
			ionoId = (String) obj.get("id");
			for (int i = 1; i <= 6; i++) {
				Bus.postIfChanged(new IonoDigitalEvent(this, "DO(" + i + ")",
						obj.get("DO" + i)));
				Bus.postIfChanged(new IonoDigitalEvent(this, "DI(" + i + ")",
						obj.get("DI" + i)));
				if (i <= 4) {
					Bus.postIfChanged(new IonoAnalogEvent(this,
							"AV(" + i + ")", obj.get("AV" + i)));
					Bus.postIfChanged(new IonoAnalogEvent(this,
							"AI(" + i + ")", obj.get("AI" + i)));
				}
			}

		}
	}

	/**
	 * 
	 * @param packet
	 * @return
	 * @throws ParseException
	 */
	private JSONObject getJsonObject(DatagramPacket packet)
			throws ParseException {
		return (JSONObject) jsonParser.parse(new String(packet.getData())
				.trim());
	}

	/**
	 * 
	 * @param message
	 * @return
	 * @throws SocketException
	 */
	private DatagramPacket send(String message) throws IOException {
		try (DatagramSocket socket = new DatagramSocket()) {
			socket.setSoTimeout(5000);
			byte[] replyBuffer = new byte[512];
			byte[] stateReq = message.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(stateReq,
					stateReq.length, ipAddress, port);
			socket.send(sendPacket);
			DatagramPacket replyPacket = new DatagramPacket(replyBuffer,
					replyBuffer.length);
			socket.receive(replyPacket);

			return replyPacket;
		}
	}

	@Override
	public boolean loop() throws InterruptedException {
		try {
			byte[] updateBuffer = new byte[512];
			DatagramPacket updatePacket = new DatagramPacket(updateBuffer,
					updateBuffer.length);
			monitorSocket.receive(updatePacket);
			if (updatePacket.getAddress().equals(ipAddress)) {
				JSONObject obj = getJsonObject(updatePacket);
				String id = (String) obj.get("id");
				if (id.equals(ionoId)) {
					int pr = ((Long) obj.get("pr")).intValue();
					if (lastPr != pr) {
						if (lastPr >= 0 && (lastPr + 1) % 10 != pr) {
							// missed something
							getState();
						}

						String pin = ((String) obj.get("pin"));
						if (pin != null) {
							pin = pin.substring(0, 2) + '(' + pin.charAt(2)
									+ ')';

							Object val = obj.get("val");

							if (pin.charAt(0) == 'D') {
								Bus.postIfChanged(new IonoDigitalEvent(this,
										pin, val));
							} else {
								Bus.postIfChanged(new IonoAnalogEvent(this,
										pin, val));
							}
						}
					}

					lastPr = pr;
				}
			}

			return true;

		} catch (SocketTimeoutException e) {
			log.error("timeout");
			return false;

		} catch (Exception e) {
			log.error("exception in loop: " + e);
			return false;
		}
	}

	@Override
	public void onQuit() throws InterruptedException {
		try {
			monitorSocket.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 * @param idx
	 * @param val
	 * @return
	 */
	public boolean setDO(int idx, boolean val) {
		return setDigital(idx, val);
	}

	/**
	 * 
	 * @param idx
	 * @param val
	 * @return
	 */
	public boolean setDO(int idx, int val) {
		return setDigital(idx, val != 0);
	}

	/**
	 * 
	 * @param idx
	 * @param val
	 * @return
	 */
	public boolean setDO(int idx, String val) {
		Boolean bVal;
		if (val.charAt(0) == 'f') {
			bVal = null;
		} else {
			bVal = val.charAt(0) != '0';
		}
		return setDigital(idx, bVal);
	}

	/**
	 * 
	 * @param idx
	 * @param val
	 * @return
	 */
	private boolean setDigital(int idx, Boolean val) {
		String sVal;
		if (val == null) {
			sVal = "f";
		} else if (val) {
			sVal = "1";
		} else {
			sVal = "0";
		}
		return setPin("DO" + idx, sVal);
	}

	/**
	 * 
	 * @param val
	 * @return
	 */
	public boolean setAO1(float val) {
		return setPin("AO1", Float.toString(val));
	}

	/**
	 * 
	 * @param pin
	 * @param val
	 * @return
	 */
	private boolean setPin(String pin, String val) {
		for (int i = 0; i < 3; i++) {
			try {
				DatagramPacket r = send(pin + "=" + val);
				String res = new String(r.getData());
				res = res.trim();
				if (res.equals("ok")) {
					return true;
				}
			} catch (IOException e) {
			}
		}

		return false;
	}
}
