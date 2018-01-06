package com.novadge.blockchain


import grails.rest.*
import grails.converters.*

class NodeController extends RestfulController {
    static responseFormats = ['json', 'xml']
    NodeController() {
        super(Node)
    }

    /**
     * To accept a list of notes in the form of urls
     */
    def register(){

    }

    /**
     * To implement our consensus algorithm, which resolves any conflicts
     * to ensure a node has the correct chain
     */
    def resolve(){

    }
}
