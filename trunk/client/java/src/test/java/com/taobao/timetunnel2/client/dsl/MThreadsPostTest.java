package com.taobao.timetunnel2.client.dsl;

import static com.taobao.timetunnel2.client.dsl.TimeTunnel2.passport;
import static com.taobao.timetunnel2.client.dsl.TimeTunnel2.tunnel;
import static com.taobao.timetunnel2.client.dsl.TimeTunnel2.use;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Test;

import com.taobao.timetunnel2.client.dsl.broker.BrokerImpl.Key;
import com.taobao.timetunnel2.client.dsl.impl.Config;
import com.taobao.timetunnel2.client.dsl.impl.Tunnel;
import com.taobao.timetunnel2.client.dsl.util.ClosedException;

/**
 * 
 * @author <a href=mailto:jiugao@taobao.com>jiugao</a>
 * @created 2010-11-18
 * 
 */
public class MThreadsPostTest extends BaseServers {

	@Test
	public void post() throws Exception {
		Config.getInstance().setRouterServerList("localhost:" + randomRouterPort);
		String brokerUrl = "{\"sessionId\":\"xxxxx\",\"brokerserver\":[\"localhost:" + port + "\"]}";
		routerImpl.setBrokerUrls(brokerUrl);

		use(passport("hello", "1111"));
		final Tunnel t = tunnel("MThreadsPostTest");
		final Tunnel t2 = tunnel("MThreadsPostTest2");
		final String content = "asfdasfadfafasfas";

		ExecutorService pool = Executors.newFixedThreadPool(10);
		final CountDownLatch latch = new CountDownLatch(500);
		for (int i = 0; i < 500; i++) {
			pool.submit(new Runnable() {
				@Override
				public void run() {
					try {
						TimeTunnel2.post(content, t);
						TimeTunnel2.post(content, t);
						TimeTunnel2.post(content, t2);
					} catch (ClosedException e) {
						throw new RuntimeException(e);
					}

					latch.countDown();
				}
			});
		}
		latch.await();
		ConcurrentHashMap<Key, Message> received = brokerImpl.getReceived();
		assertThat(received.size(), is(1500));
		for (Message m : received.values()) {
			assertThat(new String(m.getContent(), Charset.forName("utf-8")), equalTo(content));
		}
	}

	@SuppressWarnings("static-access")
	@After
	public void clear() {
		localBrokerService.stop();
		localRouterService.stop();
	}

	@Override
	void postStartBroker() {
	}

	@Override
	void postStartRouter() {
	}

	@Override
	void preStartBroker() {
	}

	@Override
	void preStartRouter() {
	}
}
