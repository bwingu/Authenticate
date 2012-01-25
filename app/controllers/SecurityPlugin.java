package controllers; import models.SecurityIdent;import models.User;import play.Play;import play.data.validation.Required;import play.libs.Crypto;import play.libs.WS;import play.libs.WS.HttpResponse;import com.google.gson.Gson;import controllers.Secure.Security; public class SecurityPlugin extends Secure.Security {    		/**	 * Méthode d'authentification .	 */    static boolean authenticate(@Required String username, @Required String password) {			String urlAuthenticate = Play.configuration.getProperty("application.urlAdmin") + "AuthenticateAction/login?username=" + username + "&password=" + password;				//Appel en webService de la partie pour l'authentification		HttpResponse res = WS.url(urlAuthenticate + username + "/" + password).get();		if(res.getStatus() != 200){					}				SecurityIdent securityIdent = new Gson().fromJson(res.getJson(), SecurityIdent.class);				if(securityIdent.identStatus){			//On mets en session le user			session.put("user", new Gson().toJson(securityIdent.userConnect).toString());			//On enregistre dans un cookie le user			response.setCookie("rememberme", Crypto.sign(username) + "-" + username, "30d");			return true;		}		return false;    }        //Permet d'obtenir le user en session	static User getUser(){		return new Gson().fromJson(session.get("user"), User.class);	}				static void logout(){	     session.clear();		 response.removeCookie("rememberme");	}}