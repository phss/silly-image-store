require "rubygems"
require "sinatra"

get "/random" do
  content_type "image/*"
  images = Dir["images/*"]
  File.open(images[rand(images.size)], "r").read
end

get "/sample" do
  "<img src='/random' />"
end