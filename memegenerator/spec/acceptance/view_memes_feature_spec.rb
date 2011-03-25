require File.expand_path(File.dirname(__FILE__) + '/acceptance_helper')

feature "View Memes Feature", %q{
  In order to see all memes in the site
  As a web user
  I want to see all the created memes on one page
} do

  background do
    @meme = Meme.create!(:meme_type => "Y_U_NO", :first_line => "First Line")
    visit '/'
  end

  scenario "Scenario name" do
    page.should have_content 'Meme Type: Y U NO'
    page.should have_content 'First Line'
    page.should have_content 'http://images2.memegenerator.net/ImageMacro/'
  end
end
