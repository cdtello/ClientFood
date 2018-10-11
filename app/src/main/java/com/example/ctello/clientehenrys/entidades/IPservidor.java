package com.example.ctello.clientehenrys.entidades;

public class IPservidor {

    private Integer id;
    private String ip;

    public IPservidor(Integer id, String ip){
        this.id = id;
        this.ip = ip;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
