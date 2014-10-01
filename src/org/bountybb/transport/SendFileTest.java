package org.bountybb.transport;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bountybb.utils.FileData;

import net.tomp2p.dht.FutureSend;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.examples.ExampleUtils;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.p2p.RequestP2PConfiguration;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;


public class SendFileTest {
    static final Random RND = new Random( 42L );
    static final String BTCKEY_1 = "1CofFmjiXLEy1MFZqiR6FqU6ud5HkeTdLK";
    static final String BTCKEY_2 = "1GZBtKL4rj4q78UKw2u5KpUJi11SCa2Mf4";
    static final Number160 BTCHASH_1 = Number160.createHash(BTCKEY_1);
    static final Number160 BTCHASH_2 = Number160.createHash(BTCKEY_2);
	public static void main(String[] args) throws Exception {
		PeerDHT master = null;
		try {
			PeerDHT[] peers = new PeerDHT[3];
			peers[0] = new PeerBuilderDHT(new PeerBuilder( new Number160( RND ) ).ports( 4001 ).start()).start();
			peers[1] = new PeerBuilderDHT(new PeerBuilder( BTCHASH_1 ).masterPeer( peers[0].peer() ).start()).start();//ExampleUtils.createAndAttachPeersDHT(100, 4001);
			peers[2] = new PeerBuilderDHT(new PeerBuilder( BTCHASH_2 ).masterPeer( peers[0].peer() ).start()).start();//ExampleUtils.createAndAttachPeersDHT(100, 4001);
			
			ExampleUtils.bootstrap(peers);
			master = peers[0];
			setupReplyHandler(peers);
			String fileName = "C:\\testing.jpg";
			exampleSendFile(peers[1], BTCHASH_2, fileName);
			Thread.sleep(10000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			master.shutdown();
		}
	}

	private static void exampleSendFile(PeerDHT from, Number160 to, String fileName) throws IOException {
		RequestP2PConfiguration requestP2PConfiguration = new RequestP2PConfiguration(1, 10, 0);
		Path path = Paths.get(fileName);
		byte[] bytes = Files.readAllBytes(path);
		FileData fd = new FileData(bytes, fileName);
		FutureSend futureSend = from.send(to).object(fd)
		        .requestP2PConfiguration(requestP2PConfiguration).start();
		futureSend.awaitUninterruptibly();
		System.err.println("got reply: " + futureSend.object());
	}


	private static void setupReplyHandler(PeerDHT[] peers) {
		for (final PeerDHT peer : peers) {
			peer.peer().objectDataReply(new ObjectDataReply() {
				@Override
				public Object reply(PeerAddress sender, Object request) throws Exception {
					System.err.println("I'm " + peer.peerID() + " and I just got the message [" + request
					        + "] from " + sender.peerId());
					FileData fd = (FileData) request;
					byte[] bytes = fd.getBytes();
					String fileName = fd.getFileName();
					FileOutputStream out = new FileOutputStream(fileName);
					out.write(bytes);
					out.close();
					return "ACK";
				}
			});
		}
	}
}
