class CreateMemes < ActiveRecord::Migration
  def self.up
    create_table :memes do |t|
      t.string :meme_type
      t.string :first_line
      t.string :second_line

      t.timestamps
    end
  end

  def self.down
    drop_table :memes
  end
end
