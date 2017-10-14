package selenium.driver;

import com.thoughtworks.selenium.DefaultSelenium;

public class MySeleniumDriver extends DefaultSelenium {

	public MySeleniumDriver(String serverHost, int serverPort,
			String browserStartCommand, String browserURL) {
		super(serverHost, serverPort, browserStartCommand, browserURL);
	}

	public void waitForElementToDisappear(String xPath, String millisString)
			throws InterruptedException {
		Long millisToWait = Long.valueOf(millisString);
		if (millisToWait < 0)
			throw new IllegalStateException(
					"Time interval cannot be negative ("
							+ millisToWait.toString() + " ms)");
		Long whenToStop = System.currentTimeMillis() + millisToWait;
		while ((this.isElementPresent(xPath))
				&& (System.currentTimeMillis() < whenToStop)) {
			Thread.sleep(500);
		}
		if (this.isElementPresent(xPath))
			throw new IllegalStateException("Element " + xPath
					+ " did not disappear after " + millisToWait.toString()
					+ " ms");
	}

	public void waitForElementToAppear(String domPath, String millisString) throws InterruptedException {
		Long millisToWait = Long.valueOf(millisString);
		if (millisToWait < 0) throw new IllegalStateException("Time interval cannot be negative (" + millisToWait.toString() + " ms)");
		Long whenToStop = System.currentTimeMillis() + millisToWait;
		while ((!(this.isElementPresent(domPath))) && (System.currentTimeMillis() < whenToStop)) {
			Thread.sleep(500);
		}
		if (!(this.isElementPresent(domPath))) throw new IllegalStateException("Element "
				+ domPath + " did not appear after " + millisToWait.toString() + " ms");
	}

	public void waitForElementToBecomeVisible(String domPath, String millisString) throws InterruptedException {
		Long millisToWait = Long.valueOf(millisString);
		if (millisToWait < 0) throw new IllegalStateException("Time interval cannot be negative (" + millisToWait.toString() + " ms)");
		Long whenToStop = System.currentTimeMillis() + millisToWait;
		waitForElementToAppear(domPath, millisToWait.toString());
		while ((!(this.isVisible(domPath))) && (System.currentTimeMillis() < whenToStop)) {
			Thread.sleep(500);
		}
		if (!(this.isVisible(domPath))) throw new IllegalStateException("Element "
				+ domPath	+ " did not become visible after " + millisToWait.toString() + " ms");
	}

	public void waitForElementToBecomeInvisible(String domPath, String millisString) throws InterruptedException {
		Long millisToWait = Long.valueOf(millisString);
		if (millisToWait < 0) throw new IllegalStateException("Time interval cannot be negative (" + millisToWait.toString() + " ms)");
		Long whenToStop = System.currentTimeMillis() + millisToWait;
		waitForElementToAppear(domPath, millisToWait.toString());
		while ((this.isVisible(domPath)) && (System.currentTimeMillis() < whenToStop)) {
			Thread.sleep(500);
		}
		if (this.isVisible(domPath)) throw new IllegalStateException("Element "
				+ domPath	+ " did not become invisible after " + millisToWait.toString() + " ms");
	}

	public void waitForVisibleElementWithPath(String xPath, String millisString)
			throws InterruptedException {
		Long millisToWait = Long.valueOf(millisString);
		if (millisToWait < 0)
			throw new IllegalStateException(
					"Time interval cannot be negative ("
							+ millisToWait.toString() + " ms)");
		Long whenToStop = System.currentTimeMillis() + millisToWait;
		waitForElementToAppear(xPath, millisToWait.toString());
		String stylePath = xPath + "/@style";
		while ((this.getAttribute(stylePath).contains("display: none;"))
				&& (System.currentTimeMillis() < whenToStop)) {
			Thread.sleep(500);
		}
		if (this.getAttribute(stylePath).contains("display: none;"))
			throw new IllegalStateException("Element " + xPath
					+ " did not become visible after "
					+ millisToWait.toString() + " ms");
	}


	public void waitForElementToBecomeEditable(String xPath, String millisString)
			throws InterruptedException {
		Long millisToWait = Long.valueOf(millisString);
		if (millisToWait < 0)
			throw new IllegalStateException(
					"Time interval cannot be negative ("
							+ millisToWait.toString() + " ms)");
		Long whenToStop = System.currentTimeMillis() + millisToWait;
		waitForElementToBecomeVisible(xPath, millisToWait.toString());
		while ((!(this.isEditable(xPath)))
				&& (System.currentTimeMillis() < whenToStop)) {
			Thread.sleep(500);
		}
		if (!(this.isEditable(xPath)))
			throw new IllegalStateException("Element " + xPath
					+ " is still disabled after " + millisToWait.toString()
					+ " ms");
	}

}
