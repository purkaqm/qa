# /*************************************************************
# * PSS QA - Helper functions library - Net
# * Version : 1.0
# * Changes :
# *
# *  06-Jan-2017 1.0 : Created.
# *
# * CopyRight :
# * All rights reserved.
# **************************************************************/

require 'json'          # JSON parser
require 'rest-client'   # REST 


def perform_get(command, headers)
   list = Hash.new
    
    begin
        response = RestClient.get(command, headers)
        list = JSON.parse(response.body)

    rescue RestClient::ExceptionWithResponse => error
        print_log(error.message)
        
    rescue SocketError => error
        print_log("Cannot connect")
    end
    
    return (list)
end