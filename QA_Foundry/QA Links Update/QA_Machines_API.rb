# /****************************************************************************
# * PSS QA important links - QA machine data.
# * Version : 0.5
# * Changes :
# *
# *
# *  14-Aug-2017 0.5 : Handle SSL error on login while getting DB version (SSO).
# *  15-May-2017 0.4 : Handle login error while getting DB version (E.g. SSO).
# *  11-May-2017 0.3 : If page cannot be reached, retry 3 times.
# *  13-Mar-2017 0.2 : Handle connection refused error.
# *  17-Dec-2016 0.1 : Created from QA_Links.rb file.
# *
# * CopyRight :
# * All rights reserved.
# ****************************************************************************/

require_relative './../QA_Functions/QA_Lib_Io'

require 'nokogiri'      # Parser
require 'open-uri'      # Wrapper for net
require 'mechanize'     # Login

# \remarks Must provide these global variables.
# \pre DEBUG_RUN
# \pre AD_credentials
# \pre context_credentials

def handle_authentication(url)
    # Handle authentication if required
    
    done = false
    use_authen = false
    error_found = false
    credentials = []
    retry_count = 3;
    
    while !done
        begin
            if (use_authen)
                page_stream = open(url, http_basic_authentication: [ credentials[0], credentials[1] ])
            else
                page_stream = open(url)
            end
            
            # Everything went smooth, so we are done
            done = true
            
        rescue OpenURI::HTTPError => error
            response = error.io
            code = response.status[0]
            
            # Unauthorized code, retry with authentication
            if (code == "401") 
                use_authen = true
                
                credentials = $AD_credentials
        
            elsif (code == "403")                
                page_stream = "Unauthorized, probably password is wrong."
                error_found = true
                done = true
            else
                page_stream = "Code = #{code} - #{response.status[1]}"
                error_found = true
                done = true
            end

        rescue SocketError, Errno::ECONNREFUSED => error
	    sleep(2)
            page_stream = "Cannot reach the site."
            error_found = true
            retry_count = retry_count - 1
            if (retry_count <= 0) then done = true end
        end
    end
    
    page_info = Hash.new
    page_info["stream"] = page_stream
    page_info["error"] = error_found
    
    return (page_info)
end

def log_in_to_app_mechanize(url)

    begin
        agent = Mechanize.new
        login_form = agent.get(url).form('form')
            
        credentials = $context_credentials
        
        login_form['user'] = credentials[0]
        login_form['pass'] = credentials[1]
        page = login_form.submit
        
    rescue Net::HTTP::Persistent::Error => error
        page = ""
    
    rescue OpenSSL::SSL::SSLError => error
        page = ""
    end
end

def log_in_to_app(url)
    log_in_to_app_mechanize(url)
end


def get_db_version(url)
    # Given an URL, open the page and get the DB version
    
    if $DEBUG_RUN
        page = Nokogiri::HTML( open("./home_source.html") )
    else
        page = log_in_to_app(url)
    end

    if (page != "")
        dbv_str = "DBVersion"
        if page.content.include?(dbv_str)
            index = page.content.index(dbv_str) + dbv_str.length + 1 
            db_version = page.content.byteslice(index, 10).strip
            db_version = db_version.split("\"", 2)[0]
        else
            db_version = ""
        end
    end
    
end

def get_context_db_version(contexts)
    # \remarks Use a reverse sorted contexts.
    # That way, if quicktest* is there it will be tested first.
    # Basically we should use a context where default password is known to work.
    contexts = contexts.sort_by {|k|}.reverse

    contexts.each do |context, details|
        if details["context_loaded"] == "Loaded"
            db_version = get_db_version(details["context_url"])
            
            # No need to go further
            if db_version != ""                
                return (db_version)
            end
        end
    end
    
    db_version = "Not found or no contexts loaded"
end

def get_contexts(page, url)
    contexts = Hash.new
    contexts_data = page.css("form[name=support]").css("tr") # From just the form that really matters
    contexts_data.each do |context_data|
        context_name = context_data.css("td.contextTableTd.first a b").text.strip
        context_url = context_data.css("td.contextTableTd.first a")[0] # It is always an array
        context_loaded = context_data.css("td.contextTableTd")[3]

        if context_name != "" && !contexts.has_key?(context_name)            
            absolute_url = context_url["href"]
            
            if !$DEBUG_RUN
                # Convert to absolute path if required
                if !context_url["href"].include?("http")
                    absolute_url = url.split("http://", 2)[1].split("/", 2)
                    absolute_url = "http://#{absolute_url[0]}#{context_url["href"]}"                
                end
            end
            
            context_details = Hash.new
            context_details["context_url"] = absolute_url
            context_details["context_loaded"] = context_loaded.text.strip
            
            contexts[context_name] = context_details
        end
    end
    
    return (contexts)
end


# End of file #