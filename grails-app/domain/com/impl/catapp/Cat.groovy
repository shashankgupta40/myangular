package com.impl.catapp

class Cat {

    String name
    String breed
    String coat
    Date dateOfArrival
	
    static mapping = {
    }
    
	static constraints = {
        name(blank:false, nullable: false)
        breed(blank:false, nullable: false)
        coat(inList:["Hairless", "Short", "Semi-long", "Long"])
        dateOfArrival(blank:false, nullable: false)
    }

}
