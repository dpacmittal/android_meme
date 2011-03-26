class MemesController < ApplicationController
  respond_to :html, :json

  def index
    @memes = Meme.all

    respond_with @memes
  end

  def create
    @meme = Meme.new(params[:meme])

    if @meme.save
      render :json => @meme, :status => :created
    else
      render :json => @meme.errors, :status => :unprocessable_entity
    end
  end

end
