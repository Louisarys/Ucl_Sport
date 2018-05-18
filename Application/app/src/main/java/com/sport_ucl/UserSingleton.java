package com.sport_ucl;

/* cette classe a pour fonction de permettre l'acces a l'objet representant l'utilisateur de
 * partout dans le programme
*/

/**
 * Created by louis on 17/03/18.
 */

public class UserSingleton {

    private static UserSingleton INSTANCE;
    private User user;


    private UserSingleton(User user){
        setUser(user);
    }

    public static UserSingleton getINSTANCE(){
        if (INSTANCE == null){
            INSTANCE = new UserSingleton(null);
        }
        return(INSTANCE);
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }
}
