require 'spec_helper'

describe "memes/new.html.erb" do
  before(:each) do
    assign(:meme, stub_model(Meme,
      :meme_type => "MyString",
      :first_line => "MyString",
      :second_line => "MyString"
    ).as_new_record)
  end

  it "renders new meme form" do
    render

    # Run the generator again with the --webrat flag if you want to use webrat matchers
    assert_select "form", :action => memes_path, :method => "post" do
      assert_select "input#meme_meme_type", :name => "meme[meme_type]"
      assert_select "input#meme_first_line", :name => "meme[first_line]"
      assert_select "input#meme_second_line", :name => "meme[second_line]"
    end
  end
end
