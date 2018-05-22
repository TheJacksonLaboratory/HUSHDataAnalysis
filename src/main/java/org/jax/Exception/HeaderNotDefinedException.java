package org.jax.Exception;

public class HeaderNotDefinedException extends RuntimeException{
    public HeaderNotDefinedException () {
        super();
    }

    public HeaderNotDefinedException(String s){
        super(s);
    }
}
