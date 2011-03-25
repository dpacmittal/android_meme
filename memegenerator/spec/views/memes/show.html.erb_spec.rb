require 'spec_helper'

describe "memes/show.html.erb" do
  before(:each) do
    @meme = assign(:meme, stub_model(Meme,
      :meme_type => "Meme Type",
      :first_line => "First Line",
      :second_line => "Second Line"
    ))
  end

  it "renders attributes in <p>" do
    render
    # Run the generator again with the --webrat flag if you want to use webrat matchers
    rendered.should match(/Meme Type/)
    # Run the generator again with the --webrat flag if you want to use webrat matchers
    rendered.should match(/First Line/)
    # Run the generator again with the --webrat flag if you want to use webrat matchers
    rendered.should match(/Second Line/)
  end
end
