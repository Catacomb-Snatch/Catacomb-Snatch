package net.catacombsnatch.game.core.multiplayer.sfs;

import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;

import com.badlogic.gdx.Gdx;
import com.smartfoxserver.v2.exceptions.SFSException;

import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.entities.Room;
import sfs2x.client.entities.User;
import sfs2x.client.requests.JoinRoomRequest;
import sfs2x.client.requests.LoginRequest;
import sfs2x.client.requests.SubscribeRoomGroupRequest;

public class SFSClient implements IEventListener {
	public final static String TAG = "[SFS]";
	
	private SmartFox sfsClient;
	
	private static final String SFS_ZONE = "CatacombSnatch";
	
	/** stored login error message */
	private String mLoginError = "";
	
	private List<User> mUsers = null;
	private List<String> mGameList = null;
	
	private int latency;
	
	private String GAME_ROOMS_GROUP_NAME = "default";
	
	private String SERVER_HOSTNAME = "sfs.catacombsnatch.net";
	//private String SERVER_HOSTNAME = "localhost";
	private int SERVER_PORT = 9933;
	
	private String username = "";
	private String password = "";
	
	public SFSClient(){
		this.initSmartFox();
	}
	
	public void connect(String username, String password){
		this.username = username;
		this.password = password;
		this.connectToServer(SERVER_HOSTNAME,SERVER_PORT);
	}
	
	private void initSmartFox(){
		// Initiate Smartfox Client
		sfsClient = new SmartFox();
		sfsClient.setDebug(false);
		// Add event listeners
		sfsClient.addEventListener(SFSEvent.CONNECTION, this);
		sfsClient.addEventListener(SFSEvent.CONNECTION_LOST, this);
		sfsClient.addEventListener(SFSEvent.LOGIN, this);
		sfsClient.addEventListener(SFSEvent.ROOM_JOIN, this);
		sfsClient.addEventListener(SFSEvent.UDP_INIT, this);
		sfsClient.addEventListener(SFSEvent.PING_PONG, this);
		
		Gdx.app.log(TAG, "Loaded");
	}
	
	/** 
	 * Clean resources
	 */
	public void destroy(){
		// Remove event handlers and disconnect from the server.
		if (sfsClient != null){
			if (sfsClient.isConnected()){
				sfsClient.disconnect();
			}
			sfsClient.removeAllEventListeners();
		}
		Gdx.app.log(TAG, "Unloaded.");
	}
	public void shutdown(){
		this.destroy();
	}
	/**
	 * Connects to SmartFoxServer
	 * 
	 * @param ip the server IP/Hostname.
	 * @param port the server port.
	 */
	private void connectToServer(final String ip, final int port){
		Gdx.app.log(TAG, "Connecting to server..");
		//threading connect so it does not lag UI
		final SmartFox sfs = sfsClient;
		new Thread(){
			@Override
			public void run(){
				sfs.connect(ip,port);
			}
		}.start();
	}
	
	/**
	 * Handle events dispatched from the SmartFoxServer
	 * @param event - the event that has been dispatched from the server.
	 * 
	 * @throws SFSExeption
	 */
	@Override
	public void dispatch(BaseEvent event) throws SFSException {
		if (event.getType().equalsIgnoreCase(SFSEvent.CONNECTION)){
			if (event.getArguments().get("success").equals(true)){
				// Connection success.
				Gdx.app.log(TAG, "Connected.");
				// now logging in
				sfsClient.send(new LoginRequest(this.username, this.password, SFS_ZONE));
			}else{
				// Connection failed.
				Gdx.app.log(TAG, "Connection failed.");
			}
		}else if(event.getType().equalsIgnoreCase(SFSEvent.CONNECTION_LOST)){
			// Connection Lost
			String reason = (String)event.getArguments().get("reason");
			Gdx.app.log(TAG, "Connection lost: " + reason);
			//sfsClient.disconnect();
		}else if (event.getType().equalsIgnoreCase(SFSEvent.LOGIN)){
			Gdx.app.log(TAG, "login succeeded.");
			if(!sfsClient.isUdpInited() && sfsClient.isUdpAvailable()){
				sfsClient.initUdp(SERVER_HOSTNAME, SERVER_PORT);
			}
			sfsClient.enableLagMonitor(true,5000);
			//check if game groups is already subscribed, if not sub it
			if (!sfsClient.getRoomManager().containsGroup(GAME_ROOMS_GROUP_NAME)){
				sfsClient.send(new SubscribeRoomGroupRequest(GAME_ROOMS_GROUP_NAME));
			}
		}else if(event.getType().equalsIgnoreCase(SFSEvent.LOGIN_ERROR)){
			mLoginError = event.getArguments().get("error").toString();
			Gdx.app.log(TAG, "Login failed: " + mLoginError);
		}else if (event.getType().equalsIgnoreCase(SFSEvent.UDP_INIT)){
			if (event.getArguments().get("success").equals(true)){
				Gdx.app.log(TAG, "UDP initialization successful!");
			}else{
				Gdx.app.log(TAG, "UDP initialization failed!");
			}
		}else if (event.getType().equalsIgnoreCase(SFSEvent.PING_PONG)){
			latency = Integer.valueOf(event.getArguments().get("lagValue").toString());
			Gdx.app.log(TAG, "latency: "+latency);
		}else if (event.getType().equalsIgnoreCase(SFSEvent.ROOM_JOIN)){
			if (mUsers != null){
				mUsers.clear();
			}
			Room room = (Room)event.getArguments().get("room");
			for (User user : room.getUserList()){
				//mUsers.add(user);
			}
			// If a game is joined, set it up
			if (room.isGame()){
				mGameList.clear();
				List<Room> GameList = sfsClient.getRoomListFromGroup(GAME_ROOMS_GROUP_NAME);
				for (Room rm : GameList){
					mGameList.add(rm.getName() + "	Users: " + rm.getUserCount() + "/" + rm.getMaxUsers());
				}
				//init the game here
				
			}else{
				//chatbox stuff
				Gdx.app.log(TAG, "Room [" + room.getName() + "] joined.");
			}
		}else if (event.getType().equalsIgnoreCase(SFSEvent.ROOM_JOIN_ERROR)){
			Gdx.app.log(TAG, "Unable to join room: "+event.getArguments().get("error").toString());
		}else if (event.getType().equalsIgnoreCase(SFSEvent.ROOM_ADD) || event.getType().equalsIgnoreCase(SFSEvent.ROOM_REMOVE)){
			updateGameList();
			Room thisRoom = (Room)event.getArguments().get("room");
			Gdx.app.log(TAG, event.getType() + ": [" + thisRoom.getName() + "]");
		}else if (event.getType().equalsIgnoreCase(SFSEvent.USER_ENTER_ROOM)){
			User user = (User)event.getArguments().get("user");
			Room room = (Room)event.getArguments().get("room");
			if (room.isGame()){
				mUsers.add(user);
				updateGameList();
			}
			Gdx.app.log(TAG, "User " + user.getName() + " joined room " + room.getName());
		}else if (event.getType().equalsIgnoreCase(SFSEvent.USER_EXIT_ROOM)){
			User user = (User)event.getArguments().get("user");
			Room room = (Room)event.getArguments().get("room");
			mUsers.remove(user.getName());
			if (room.isGame()){
				updateGameList();
			}
			Gdx.app.log(TAG, "User " + user.getName() + " left room " + room.getName());
		}else if (event.getType().equalsIgnoreCase(SFSEvent.PUBLIC_MESSAGE)){
			Room room = (Room)event.getArguments().get("room");
			User sender = (User)event.getArguments().get("sender");
			String message = event.getArguments().get("message").toString();
			Gdx.app.log(TAG, "Chat #"+room.getName()+" <"+sender.getName()+"> "+message);
		}
		
	}
	
	/**
	 * Update game list
	 */
	public void updateGameList(){
		mGameList.clear();
		List<Room> gameList = sfsClient.getRoomListFromGroup(GAME_ROOMS_GROUP_NAME);
		ListIterator<Room> gameRoomIterator = gameList.listIterator();
		while (gameRoomIterator.hasNext()){
			Room room = gameRoomIterator.next();
			//Add each room back into the adapter with player count
			mGameList.add(room.getName() + "	Users: " + room.getUserCount()+"/"+room.getMaxUsers());
		}
	}
	
	public int getLatency(){
		return latency;
	}
	
}
