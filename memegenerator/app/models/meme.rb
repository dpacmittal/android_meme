class Meme < ActiveRecord::Base
  validates_presence_of :meme_type, :first_line

end
