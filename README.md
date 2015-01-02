# silly-image-store

Image store written in Clojure. It allows:

* Listing images
* Viewing an image
* Viewing a random image
* ???
* Profit

Serving images from a Java application is probably not the most efficient way of doing it. So don't use this for a production application. :)

## Routes

    / - list options (images, buckets, random image)
    /images - list images
    /images/:image - show an image
    /random - show a random image
    /buckets - list buckets
    /buckets/:bucket - list bucket options (images, random image)
    /buckets/:bucket/images - list bucket images
    /buckets/:bucket/images/:image - show a bucket image
    /buckets/:bucket/random - show a random bucket image


## Testing

    lein test
    
    
## Building

    lein ring uberjar

## Running

From the project
    
    lein ring server
    
From the jar

    java -Dbase.store.dir=<path to image files> -jar silly-image-store-*-SNAPSHOT-standalone.jar
