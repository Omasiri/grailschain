package com.novadge.blockchain;

import java.io.Serializable;
import java.util.ArrayList;

public class Block implements Serializable{
    long index;
    long timestamp;
    long proof;
    String previousHash;
    ArrayList<Transaction> transactions; //= new ArrayList<>();

    public Block(long index, long timestamp, long proof, String previousHash){
        transactions = new ArrayList<>();
        this.index = index;
        this.timestamp = timestamp;
        this.proof = proof;
        this.previousHash = previousHash;

    }


    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public long getIndex() {
        return index;
    }

    public long getProof() {
        return proof;
    }
}
