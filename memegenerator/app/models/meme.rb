require 'net/http'
require 'cgi'
require 'nokogiri'

class Meme < ActiveRecord::Base
  validates_presence_of :meme_type
  validate :text_lines

  before_save :generate_meme_image

  MEMES = [
    { :generatorType => 'Y_U_NO', :templateID => 147676, :generatorName => 'Why-you-no' },
    { :generatorType => 'XZIBIT', :templateID => 3114, :generatorName => 'XZIBIT' }
  ]

private
  def text_lines
    self.first_line = " " if self.first_line.nil?
    self.second_line = " " if self.second_line.nil?
  end

  def generate_meme_image
    url = URI.parse 'http://memegenerator.net/Instance/CreateOrEdit'

    # Match the proper MEME
    meme = MEMES.find { |m| m[:generatorType] == meme_type }
    if meme.nil?
      errors.add :meme_type, "Invalid meme type"
      return false
    end

    post_data = { 
      'templateType' => "AdviceDogSpinoff",
      'templateID' => meme[:templateID],
      'generatorName' => meme[:generatorName]
    }

    # Add text lines
    post_data.merge! "text0" => self.first_line
    post_data.merge! "text1" => self.second_line

    res = nil
    location = nil
    Net::HTTP.start url.host do |http|
      post = Net::HTTP::Post.new url.path
      post.set_form_data post_data
      res = http.request post

      location = res['Location']
      redirect = url + location

      get = Net::HTTP::Get.new redirect.request_uri
      res = http.request get
    end
    doc = Nokogiri.HTML res.body
    self.image_url = doc.css("a[href=\"#{location}\"] img").first['src']
  end

end
