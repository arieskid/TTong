package jay.media;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import org.sipdroid.net.RtpPacket;
import org.sipdroid.net.RtpSocket;
import jay.dencode.Decoder;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class LanAudioPlay extends Thread
{
	private final static String TAG = "LanAudioPlay";

	protected AudioTrack m_out_trk;
	private volatile Thread runner;
	private Decoder decoder;
	protected DatagramSocket udp_socket;
	protected RtpPacket rtp_packet;
	protected RtpSocket rtp_socket;
	protected int SampleRate = 16000;
	// protected int listenport;
	protected final int mFrameSize = 320;
	protected final int Rtphead = 12;
	protected final int GO_TIMEOUT = 1000;
	protected int codectype = 1;
	protected byte[] m_out_bytes;
	protected int m, vm = 1;

	// speech preprocessor
	/** ��ǰ��ȡ�����к� */
	protected int gseq = 0;

	/** ��һ����Ч���к� */
	protected int currentseq = 0;

	/**
	 * ��ǰ��ȡ�����кŵĵͰ�λ getseq = gseq & 0xff;
	 */
	protected int getseq;
	protected int expseq;

	/** ��ȡ���������кŲ� ����϶�� */
	protected int gap;

	/** ����������������� ��sipUAĬ��20�� */
	protected int ec_buffer_pkgs = 0;

	public static float good, late, lost, loss, loss2;

	// used for echo calc

	public LanAudioPlay(DatagramSocket socket, int codectype, int SampleRate, int ec_buffer_pkgs)
	{
		Log.i(TAG, "new LanAudioPlay() socket port=" + socket.getPort());

		try {
			this.SampleRate = SampleRate;
			this.codectype = codectype;
			this.ec_buffer_pkgs = ec_buffer_pkgs;
			int m_out_buf_size = AudioTrack.getMinBufferSize(SampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT);
			m_out_trk = new AudioTrack(AudioManager.STREAM_VOICE_CALL, SampleRate,
					AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, m_out_buf_size * 2/* 10 */,
					AudioTrack.MODE_STREAM);
			udp_socket = socket;
			Log.i(TAG, "Audio track m_out_buf_size is " + m_out_buf_size);
			// m_out_trk.

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startThread()
	{
		if (runner == null) {
			runner = new Thread(this);
			runner.start();
		}
	}

	public void stopThread()
	{
		if (runner != null) {
			Thread moribund = runner;
			runner = null;
			moribund.interrupt();
			this.free();
		}
	}

	public void run()
	{
		Log.d(TAG, "LanAudioPlay running..");

		// byte[] buffer = new byte[mFrameSize + Rtphead];
		byte[] buffer = new byte[1024 + Rtphead];

		rtp_packet = new RtpPacket(buffer, 0);
		rtp_packet.setPayloadType(codectype);

		try {
			rtp_socket = new RtpSocket(this.udp_socket);
		} catch (Exception e) {
			e.printStackTrace();
		}
		decoder = new Decoder(codectype, ec_buffer_pkgs);
		decoder.startThread();

		Log.d(TAG, "#### 1");
		try {
			rtp_socket.receive(rtp_packet);// ����Rtp������
			Log.i(TAG, "@@@@@@@@ rtp_socket receive port: " + rtp_socket.getDatagramSocket().getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.d(TAG, "#### 2");
		m_out_trk.play();
		Log.d(TAG, "#### 3");
		System.gc();
		Log.d(TAG, "#### 4");
		empty();

		while (Thread.currentThread() == runner) {
			long ms = System.currentTimeMillis();

			try {
				rtp_socket.receive(rtp_packet);// ����Rtp������
			} catch (IOException e) {
				e.printStackTrace();
			}

			gseq = rtp_packet.getSequenceNumber();// rtp�����к�[]
													// rtp_packet.setSequenceNumber(seqn++)
			if (currentseq == gseq) {// ��һ����Ч���к�
				m++;
				continue;
			}
			lostandgood();
			// Log.d("LanAudioPlay", "lost:" + lost+" good:"+good);// �����ʣ�

			if (decoder.isIdle()) {
				decoder.putData(System.currentTimeMillis(), buffer, Rtphead, rtp_packet.getPayloadLength());// д����뻺��
				// Log.i(TAG, "Write " + rtp_packet.getPayloadLength() +
				// " size data to decoder");
			}

			// if (decoder.isGetData() == true) {
			while (decoder.isGetData() == true) {/* ���������������ݵ�ʱ�򣬾ͽ���ȡ�� */
				short[] s_bytes_pkg = decoder.getData().clone();
				m_out_trk.write(s_bytes_pkg, 0, s_bytes_pkg.length);// д�벥�Ż���
			}
			//Log.e(TAG, " ��������д��һ��ʱ��   " + (System.currentTimeMillis() - ms));
		}
		Log.d("LanAudioPlay", "lost:" + lost + " good:" + good);// �����ʣ�
	}

	void empty()
	{
		Log.d(TAG, "empty()");
		try {
			rtp_socket.getDatagramSocket().setSoTimeout(1);
			for (;;)
				rtp_socket.receive(rtp_packet);
		} catch (IOException e) {
		}
		try {
			rtp_socket.getDatagramSocket().setSoTimeout(GO_TIMEOUT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		currentseq = 0;
	}

	void lostandgood()
	{
		if (currentseq != 0) {
			getseq = gseq & 0xff;
			expseq = ++currentseq & 0xff;
			if (m == LanAudioRecord.m)
				vm = m;
			gap = (getseq - expseq) & 0xff;
			if (gap > 0) {
				if (gap > 100)
					gap = 1;
				loss += gap;
				lost += gap;
				good += gap - 1;
				loss2++;
			} else {
				if (m < vm) {
					loss++;
					loss2++;
				}
			}
			good++;
			if (good > 110) {
				good *= 0.99;
				lost *= 0.99;
				loss *= 0.99;
				loss2 *= 0.99;
				late *= 0.99;
			}
		}
		m = 1;
		currentseq = gseq;
	}

	public void free()
	{
		m_out_trk.stop();
		decoder.stopThread();
	}

}