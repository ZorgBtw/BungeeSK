<h2 align="center">BungeeSK</h2> 
BungeeSk is a Skript addon that allows you to communicate with a bungeecord proxy easily !

<br>

## 📥 How to download and install ?
You can download the last available version of BungeeSK by clicking [here](https://github.com/ZorgBtw/BungeeSK/releases/latest).
Then you can drop your `BungeeSK.jar` file in your Bungeecord `plugins/` folder and in every Spigot `plugins/` folder too. Make sure you got Skript running on your Spigot servers. (If not, you can download Skript [here](https://github.com/SkriptLang/Skript/releases/latest))

## 🚀 How to run correctly BungeeSK ?
First, you will need to connected your Spigot servers (called as clients) to your Bungeecord proxy (call as server).
Here's an example of code to link everything:
```py
on server start:
	while client is not connected: #Using a while loop to try to connect if the client is not connected
		create new bungee connection: #Creation of a new Bungeecord connection
			set address of connection to "127.0.0.1" #Use this IP if the Bungeecord is on the same machine
			set port of connection to 100 #This port as to be opened if the specified client is not hosted on the same machine as the server
			set password of connection to "Strong password" #Complete your password here, this has to be the same as the one in the Bungeecord config
			set name of connection to "hub" #Name has to be unique, if the same name is already connected, the connection will end
		start new connection with last created connection #Sending connection request to the server
	wait 30 seconds #Waiting 30 seconds between 2 connection tries, it's recommended to not decrease this value
```

## 📚 Need support or be informed ?
- [**Discord server**](https://discord.gg/PCnyMDsTRA)
- Documentations:<br>
<a href="http://skripthub.net/docs/?addon=BungeeSK"> <img src="http://skripthub.net/static/addon/ViewTheDocsButton.png" height="75"></img></a>
<a href="https://docs.skunity.com/syntax/search/addon:bungeesk"> <img src="https://skunity.com/branding/buttons/get_on_docs_3.png" height="75"></img></a>
