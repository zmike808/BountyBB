package hello.world

	public class MyApplication extends FredPlugin {

		private final static Logger LOGGER = Logger.getLogger(MyApplication.class.getName());

		//the entry point to your application, the first method that is called when loading your application in Freenet
		public void runPlugin(PluginRespirator pr)
		{
			setupWebInterface();
		}

		//method called when your plugin is shutdown
		public void terminate()
		{
		
		}

		private void setupWebInterface()
		{
				// TODO: remove
			PluginContext pluginContext = new PluginContext(pr);
			this.webInterface = new WebInterface(pluginContext);

			pr.getPageMaker().addNavigationCategory(basePath + "/","WebOfTrust.menuName.name", "WebOfTrust.menuName.tooltip", this);
			ToadletContainer tc = pr.getToadletContainer();
		
			// pages
			Overview oc = new Overview(this, pr.getHLSimpleClient(), basePath, db);
		
			// create fproxy menu items
			tc.register(oc, "WebOfTrust.menuName.name", basePath + "/", true, "WebOfTrust.mainPage", "WebOfTrust.mainPage.tooltip", WebOfTrust.allowFullAccessOnly, oc);
			tc.register(oc, null, basePath + "/", true, WebOfTrust.allowFullAccessOnly);
		
			// register other toadlets without link in menu but as first item to check
			// so it also works for paths which are included in the above menu links.
			// full access only will be checked inside the specific toadlet
			for(Toadlet curToad : newToadlets) {
				tc.register(curToad, null, curToad.path(), true, false);
			}
		
			// finally add toadlets which have been registered within the menu to our list
			newToadlets.add(oc);

		}
}

	public abstract class FileReaderToadlet extends Toadlet implements LinkEnabledCallback {

		protected String path;
		protected String filePath;
	
		public FileReaderToadlet(HighLevelSimpleClient client, String filepath, String URLPath) {
			this.path = URLPath;
			this.filePath = filepath;
			this.db = db;
		}

		//this handles an HTTP GET to the path exposed in the method path()	
		public void handleMethodGET(URI uri, HTTPRequest request, ToadletContext ctx) throws ToadletContextClosedException, IOException {
				writeHTMLReply(ctx, 200, "pageContent", "Hello world!");
		}
	
		//the relative URL that this page is at (i.e. /my_app/mypage)
		@Override
		public String path() {
			return path;
		}
	}