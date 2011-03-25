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

    it "Should require a first line" do
      @meme.save
      @meme.errors[:first_line][0].should == "can't be blank"
    end

    it "Should generate a image url when created" do
      debugger
      @meme = Meme.create(:meme_type => "Y_U_NO", :first_line => "first line")

#http://images1.memegenerator.net/ImageMacro/6532110/Y-U-NO-dsfa.jpg?imageSize=Medium&generatorName=Y-U-NO
      
    end

    it "Should fail on multi-line memes with only one line provided" do

    end
  end

end
