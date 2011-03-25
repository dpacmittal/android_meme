class MemesController < ApplicationController
  respond_to :html, :json

  def index
    @memes = Meme.all

    respond_with @memes
  end

  def create
#    @meme = Meme.new(params[:meme])
#
#    respond_to do |format|
#      if @meme.save
#        format.json  { render :json => @meme, :status => :created }
#      else
#        format.json  { render :json => @meme.errors, :status => :unprocessable_entity }
#      end
#    end
  end

end
