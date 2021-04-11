/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cw2_pong;

import java.io.Serializable;
import java.net.InetAddress;

/**
 *
 * @author karishma
 */
public class Player implements Serializable {
    private InetAddress ip;
    private int port;
    private int xPosition;
    private int yPosition;
    private int score;

    //in case you want to reate object and then add parameters
    public Player() {
    }

    public Player(int xPostion, int yPosition, int score, InetAddress ip, int port) {
        this.xPosition = xPostion;
        this.yPosition = yPosition;
        this.score = score;
        this.ip = ip;
        this.port = port;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getxPostion() {
        return xPosition;
    }

    public void setxPostion(int xPostion) {
        this.xPosition = xPostion;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    public void setPostion(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
}
