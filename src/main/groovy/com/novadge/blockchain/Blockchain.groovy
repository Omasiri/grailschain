package com.novadge.blockchain

import groovy.json.JsonSlurper;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.security.MessageDigest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.*;
import java.io.*;

public class Blockchain {
   private ArrayList<Block> chain;
   private ArrayList<Transaction> transactions;
    private HashSet<String> nodes;
   public Blockchain(){
       chain = new ArrayList<>();
       transactions = new ArrayList<>();
       nodes = new HashSet<>();

       // create the genesis block
       newBlock(1,"100");

   }

    /**
     * Return the chain of transactions
     * @return
     */
    public ArrayList<Block> getChain() {
        return chain;
    }

    /**
     * Return a list of transactions
     * @return
     */
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Return a list of registered blockchain servers/nodes
     * @return
     */
    public HashSet<String> getNodes(){
        return nodes;
    }
    /**
     * Add a new node to the list of nodes
     * @param address: address of the node eg. 'http://192.168.1.1:5000'
     */
    public void registerNode(String address){

        URL url;

        try{
            url = new URL(address);
            nodes.add(url.toString());
        }
        catch (MalformedURLException ex){
            //Todo: do something relevant
        }




    }

    /**
     * Determine if a given blockchain is valid
     * @param chain
     */
    public boolean validChain(ArrayList<Block> chain){

        Block lastCheckedBlock = chain.get(0);
        int currentIndex = 1;
        while(currentIndex < chain.size()){
            Block block = chain.get(currentIndex);
            System.out.println(""+lastCheckedBlock);
            System.out.println(""+block);

            // check that the hash of the block is correct
            if(!block.getPreviousHash().equals(hash(lastCheckedBlock))){
               return false;
            }

            // check that the proof of work is correct
            if(!validProof(lastCheckedBlock.getProof(),block.getProof())){
                return false;
            }

            lastCheckedBlock = block;
            currentIndex++;
        }

        return true;
    }

    /**
     * This is our consensus algorithm, it resolves conflicts by replacing
     * our chain with the longest one in the network
     * @return boolean : true if our chain was replaced, false otherwise
     */
    public boolean resolveConflicts(){
        HashSet neighbours = nodes;
        ArrayList<Block> newChain = null;

        // We're only looking for chains longer than ours
        int maxLength = chain.size();

        // Grab and verify the chains from all the nodes in our network
        for(String node: nodes){
            String addr = node+"/chain"
            URL url = new URL(addr);
            JsonSlurper slurper = new JsonSlurper()
            def resp = slurper.parseText(url.getText())
            if(resp.length){
                // todo: implement properly
                def length = resp.length;
                ArrayList<Block> chain = resp.chain;

                if(length > maxLength && validChain(chain)){
                    maxLength = length;
                    newChain = chain;
                }
            }
        }

        if(newChain){
            this.chain = newChain;
            return true;
        }

        return false;
    }

    /**
     * Creates a new block in the blockchain
      * @param proof : The proof given by the proof of work algorithm
     * @param previousHash : Optional hash of the  previous block
     * @return
     */
   public Block newBlock(long proof,String previousHash){

       long index = this.chain.size();
       long timestamp = System.currentTimeMillis();
       String hash = "";

       if(!previousHash.equals("") || previousHash != null){
           hash = previousHash;
       }
       else{
           hash = hash(chain.get(chain.size()-1));
       }
       Block block = new Block(index,timestamp,proof,hash);
       block.setTransactions(this.transactions);
       // reset the current list of transactions
       this.transactions = new ArrayList<>();

       // add new block to the chain
       this.chain.add(block);
       return block;

   }

    /**
     * Creates a new transaction and adds it to the list of transactions
     * @param : sender : address of the sender
     * @param : recipient : address of the recipient
     * @param : amount : amount
     *
     */
   public long newTransaction(String sender, String recipient, BigDecimal amount){

       Transaction transaction = new Transaction(sender, recipient,amount);
       transactions.add(transaction);
       return lastBlock().getIndex() + 1;
   }

    /**
     * Creates a SHS-256 hash of a Block
     * @param
     */
   public static String hash(Block block){
       return getSHA256(block.toString());


   }


   public static String getSHA256(String text){
       MessageDigest digest;
       byte[] hash;
       String hashString = "";
       try{
           digest = MessageDigest.getInstance("SHA-256");
           hash = digest.digest(text.getBytes());
           hashString =  new  BigInteger(1, hash).toString(16);
       }
       catch (Exception ex){


       }



       return hashString;
   }

    /**
     * Returns the last block in the chain
     * @return
     */
   public Block lastBlock(){
       return this.chain.get(chain.size()-1);
   }


    /**
     * A simple proof of work algorithm
     * find a number p such that hash(pp') contains leading 4 zeros, where
     * pp is the previous proof, and p' is the new proof
     * @param lastProof
     * @return
     */
   public long proofOfWork(long lastProof){


       long proof = 0;
       while(!validProof(lastProof,proof)){
           System.out.println("Checking proof "+proof);
           proof++;
       }

       return proof;

   }

    /**
     * Validates the proof: Does hash( lastProof, proof contain 4 leading zeros
     * @param lastProof
     * @param proof
     * @return
     */
   public static boolean validProof(long lastProof, long proof){

       String guess = ""+lastProof+""+proof;
       String guessHash = "";


       MessageDigest digest;
       byte[] hash;
       try{
           digest = MessageDigest.getInstance("SHA-256");
           hash = digest.digest(guess.getBytes());
           guessHash =  new  BigInteger(1, hash).toString(16);
       }
       catch (Exception ex){


       }

       return guessHash.endsWith("0000");
   }


}
