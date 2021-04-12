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
    private int playerPort;
    private double xPosition;
    private double yPosition;
    private int score;

    //in case you want to reate object and then add parameters
    public Player() {
    }

    public Player(double xPostion, double yPosition, int score, InetAddress ip, int playerPort) {
        this.xPosition = xPostion;
        this.yPosition = yPosition;
        this.score = score;
        this.ip = ip;
        this.playerPort = playerPort;
    }

    

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public int getPlayerPort() {
        return playerPort;
    }

    public void setPlayerPort(int playerPort) {
        this.playerPort = playerPort;
    }

    public double getxPostion() {
        return xPosition;
    }

    public void setxPostion(double xPostion) {
        this.xPosition = xPostion;
    }

    public double getyPosition() {
        return yPosition;
    }

    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    public void setPostion(double xPosition, double yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
    
    @Override
    public String toString() {
        return "Player{" + "ip=" + ip + ",\nplayerPort=" 
                + playerPort + ",\nxPosition=" + xPosition 
                + ",\nyPosition=" + yPosition + ",\nscore=" + score + "\n}";
    }
}
