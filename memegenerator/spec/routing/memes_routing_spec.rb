require "spec_helper"

describe MemesController do
  describe "routing" do

    it "recognizes and generates #index" do
      { :get => "/memes" }.should route_to(:controller => "memes", :action => "index")
    end

    it "recognizes and generates #new" do
      { :get => "/memes/new" }.should route_to(:controller => "memes", :action => "new")
    end

    it "recognizes and generates #show" do
      { :get => "/memes/1" }.should route_to(:controller => "memes", :action => "show", :id => "1")
    end

    it "recognizes and generates #edit" do
      { :get => "/memes/1/edit" }.should route_to(:controller => "memes", :action => "edit", :id => "1")
    end

    it "recognizes and generates #create" do
      { :post => "/memes" }.should route_to(:controller => "memes", :action => "create")
    end

    it "recognizes and generates #update" do
      { :put => "/memes/1" }.should route_to(:controller => "memes", :action => "update", :id => "1")
    end

    it "recognizes and generates #destroy" do
      { :delete => "/memes/1" }.should route_to(:controller => "memes", :action => "destroy", :id => "1")
    end

  end
end
