# /*************************************************************
# * PSS QA - Wiki API
# * An interface to Confluence using REST.
# * Version : 1.2
# * Changes :
# *
# *  13-Mar-2017 1.2 : Removed white spaces between nodes.
# *  19-Dec-2016 1.1 : Added updating wiki page feature.
# *  17-Dec-2016 1.0 : Created.
# *
# * CopyRight :
# * All rights reserved.
# **************************************************************/

require_relative './../QA_Functions/QA_Lib_Io'

require 'rest-client'   # GET/PUT
require 'nokogiri'      # Parsing and modifying
require 'json'


#wiki_base_url = "https://uplandsoftware.atlassian.net/wiki/rest/api"
#id = 108789900


def get_wiki_page_content(base_url, id, auth_code)
    list = Hash.new
    page = ""
    
    command = base_url + "/content/#{id}"
    begin
        response = RestClient.get( command, 
                                  {:Authorization => "Basic #{auth_code}",
                                  :params => {:expand => "body.storage,version"} }
                            )
        
        list = JSON.parse(response.body)
        page = list["body"]["storage"]["value"]
        page_version = list["version"]["number"]
        page_title = list["title"]
        
    rescue RestClient::ExceptionWithResponse => error
        puts error.response
        
    rescue SocketError => error
        puts "Cannot connect"
    end
    
    return ( {"page" => page, "page_version" => page_version, "title" => page_title} )
end

def wiki_debug_write_file(filename, content)
    open(filename, "w") do |file|
        file.puts content
    end
end

def wiki_format_links(links_list)
    # Convert the given list of links into Confluence storage format
    # 
    # Input format  - 
    #   [ 
    #        [ <name> | <link> ]
    #        [ <name> | <link> ]
    #   ]
    #
    # Output format -
    # <p>
    #   <a></a>
    #   <a></a><br/>
    # </p>
    # Anchors is - 
    # <a class="external-link" href="<URL>" rel="nofollow" >
    # 
    # Do not add any whitespaces as we will be using storage format. 
    #
    
    links_str = "<p>"
    
    links_list.each do |link|        
        link = link.split("|", 2)
        name = link[0].delete("[]").strip
        url = link[1].delete("[]").strip
        a_str = "<a class='external-link' href='#{url}' rel='nofollow' >#{name}</a>"
        links_str += a_str
        
        links_str += "<br/>"
    end
    
    links_str += "</p>"
    
    return(links_str)
end

def wiki_update_page_content(base_url, id, auth_code, title, content, version)
    # See https://docs.atlassian.com/atlassian-confluence/REST/latest/#content-update
    
    # Be sure to strip all the white spaces between the HTML nodes.
    # Otherwise viewing in HTML would be fine but when editing those will show up.
    # And it would look weird. 
    # Although the only time editing is required would be when updating server 
    # source details. Like say adding a new machine or updating URL.
    #
    # Since this is specific to JIRA storage representation, handle just before
    # the actual update.
    #
    if content.class != "String"
        content = content.to_s
    end    
    wiki_debug_write_file("content.xml", content)
    content = content.gsub(/>\s+</, "><")
    wiki_debug_write_file("content_out.xml", content)
    
    # Construct the JSON data
    page_schema = Hash.new
    page_schema["id"] = id
    page_schema["version"] = {"number" => version.to_i + 1}
    page_schema["type"] = "page"
    page_schema["title"] = title
    page_schema["body"] = {"storage" => {"value" => content, "representation" => "storage"} }
    
    command = base_url + "/content/#{id}"
    begin
        response = RestClient.put( command, page_schema.to_json,
                                  {:Authorization => "Basic #{auth_code}",
                                   :content_type => 'application/json'
                                  }
                            )
                
        list = JSON.parse(response.body)
        puts list["statusCode"]
        page = list["body"]["storage"]["value"]
        page_version = list["version"]["number"]
        
    rescue RestClient::ExceptionWithResponse => error
        puts error.response
        
    rescue SocketError => error
        puts "Cannot connect"
    end
end

## Test functions ##
#$DEBUG_RUN = true

def test_wiki_update_page_content()
    wiki_base_url = "https://uplandsoftware.atlassian.net/wiki/rest/api"
    id = "68452536" # Scratch pad
    title = "Scratch pad"
    content = "<p>Hello</p>"
    version = 4
    wiki_update_page_content(wiki_base_url, id, get_auth_code("JIRA"), title, content, version)
end 
#test_wiki_update_page_content()

def test_get_wiki_page_content()
    wiki_base_url = "https://uplandsoftware.atlassian.net/wiki/rest/api"
    id = "108789900" # QA Machine Info
    page_details = get_wiki_page_content(wiki_base_url, id, get_auth_code("JIRA") )
end 
#test_get_wiki_page_content()

def test_wiki_page_update_integration()
    wiki_base_url = "https://uplandsoftware.atlassian.net/wiki/rest/api"
    id = "108789900" # QA Machine Info
    page_details = get_wiki_page_content(wiki_base_url, id, get_auth_code("JIRA") )
    
    content = page_details["page"]
    title = "Scratch pad"    
    version = 4   
    id = "68452536" # Scratch pad
    wiki_update_page_content(wiki_base_url, id, get_auth_code("JIRA"), title, content, version)
end 
#test_wiki_page_update_integration()

# End of file #
