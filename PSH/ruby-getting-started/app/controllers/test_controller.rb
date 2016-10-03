class testController < ApplicationController
   def id
   end
 
   def test_params
   params.require(:id)
   end
end