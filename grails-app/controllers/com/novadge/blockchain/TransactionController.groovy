package com.novadge.blockchain


import grails.rest.*
import grails.converters.*

class TransactionController extends RestfulController {
    static responseFormats = ['json', 'xml']
    TransactionController() {
        super(Transaction)
    }

    Blockchain blockchain = new Blockchain();
    static String nodeIdentifier = UUID.randomUUID().toString().replace("-","")

    /**
     * Create a new transaction to a block
     */
    def create(){

        if(!params.sender || !params.recipient || !params.amount){
            render status: 400
            return
        }
        long index = blockchain.newTransaction(params.sender,params.recipient,params.amount as BigDecimal);

        render "Transaction will be added to block ${index}"
    }

    /**
     * Mine a new block
     */
    def mine(){

        // we must run the proof of work algorithm to get the next proof
        Block lastBlock = blockchain.lastBlock();
        long lastProof = lastBlock.getProof();
        def proof = blockchain.proofOfWork(lastProof)
        // we must receive a reward for finding the proof
        //The sender is "0" to signify that this node has mined a new coin.
        blockchain.newTransaction("0",nodeIdentifier,1.0);

        // forge the new block by adding it to the chain
        String prevousHash = blockchain.hash(lastBlock);
        Block block = blockchain.newBlock(proof,prevousHash);

        Map resp = [:]
        resp.message = "New block forged"
        resp.index = block.getIndex()
        resp.transactions = block.getTransactions()
        resp.proof = block.getProof()
        resp.previousHash = block.getPreviousHash()

        render resp as JSON


    }

    /**
     * Return the full blockchain
     */
    def chain(){

//        render "We will return the entire chain here..."

        Map resp = [:]
        resp.chain = blockchain.getChain()
        resp.length = blockchain.getChain().size()
        render resp as JSON
    }

    /**
     * register nodes on the blockchain
     */
    def registerNode(){

        //TODO : move this to node controller
        if(!params.nodes){
            render status: 401, message:"Please supply a valid list of nodes"
            return
        }

        for(String node:params.nodes){
            blockchain.registerNode(node);
        }

        Map resp = [:]
        resp.message = "new nodes have been added"
        resp.totalNodes = blockchain.getNodes()

        render resp as JSON
    }


    def consensus(){

        // TODO : move to node controller
        boolean  replaced = blockchain.resolveConflicts();
        Map resp = [:]
        if(replaced){

            resp.message = "Our chain was replaced"
            resp.newChain = blockchain.getChain();

        }
        else{
           resp.message = "Our chain is authoritative"
            resp.chain = blockchain.getChain();
        }

        render resp as JSON
    }
}
