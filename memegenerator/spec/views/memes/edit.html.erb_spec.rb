require 'spec_helper'

describe "memes/edit.html.erb" do
  before(:each) do
    @meme = assign(:meme, stub_model(Meme,
      :meme_type => "MyString",
      :first_line => "MyString",
      :second_line => "MyString"
    ))
  end

  it "renders the edit meme form" do
    render

    # Run the generator again with the --webrat flag if you want to use webrat matchers
    assert_select "form", :action => memes_path(@meme), :method => "post" do
      assert_select "input#meme_meme_type", :name => "meme[meme_type]"
      assert_select "input#meme_first_line", :name => "meme[first_line]"
      assert_select "input#meme_second_line", :name => "meme[second_line]"
    end
  end
end
