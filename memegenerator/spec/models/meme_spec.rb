require 'spec_helper'

describe Meme do

  before(:each) do
      @meme = Meme.create()
  end

  describe 'Creating' do
    it "Should require a meme type" do
      @meme.save
      @meme.errors[:meme_type][0].should == "can't be blank"
    end

    it "Should fail if a bad meme type is used" do
      @meme.meme_type = "INVALID"
      @meme.first_line = "first"
      @meme.second_line = "second"
      @meme.save
      @meme.errors[:meme_type][0].should == "Invalid meme type"
    end

    it "Should generate a image url when created" do
      @meme.meme_type = "Y_U_NO"
      @meme.first_line = "first"
      @meme.second_line = "second"
      @meme.save
      @meme.image_url.should be
      @meme.image_url.match(/memegenerator.net/).should be
      @meme.image_url.match(/first-second.jpg/).should be
    end
  end

end
