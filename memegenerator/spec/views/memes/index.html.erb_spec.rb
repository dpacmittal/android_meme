require 'spec_helper'

describe "memes/index.html.erb" do
  before(:each) do
    assign(:memes, [
      stub_model(Meme,
        :meme_type => "Meme Type",
        :first_line => "First Line",
        :second_line => "Second Line"
      ),
      stub_model(Meme,
        :meme_type => "Meme Type",
        :first_line => "First Line",
        :second_line => "Second Line"
      )
    ])
  end

  it "renders a list of memes" do
    render
    # Run the generator again with the --webrat flag if you want to use webrat matchers
    assert_select "tr>td", :text => "Meme Type".to_s, :count => 2
    # Run the generator again with the --webrat flag if you want to use webrat matchers
    assert_select "tr>td", :text => "First Line".to_s, :count => 2
    # Run the generator again with the --webrat flag if you want to use webrat matchers
    assert_select "tr>td", :text => "Second Line".to_s, :count => 2
  end
end
