# /*************************************************************
# * PSS QA important links
# * Version : 2.2
# * Changes :
# *
# *  13-Mar-2017 2.1 : Remove unwanted whitespaces from Wiki content.
# *  21-Dec-2016 2.1 : Uses links from wiki page itself.
# *                     Uses single credentials only. 
# *                     Refactored for simplicity. 
# *  17-Dec-2016 2.0 : Integration with Confluence wiki.
# *                     Refactored into a separate files.
# *  14-Dec-2016 1.2 : Corrected blank AD credential which led to authentication failure.
# *  10-Dec-2016 1.1 : Handles and continues if authentication was wrong.
# *  06-Dec-2016 1.0 : First release.
# *  06-Dec-2016 0.6 : Details are now written to an output file.
#                       Unreachable sites are now properly handled.
#                       DB version is retried with other loaded contexts if first one fails.
#                       Added switch to ask credentials only once.
# *  01-Dec-2016 0.5 : Java version is now displayed.
# *                     Can now read machines from an input file.
# *                     Refactored and cleaned up. 
# *  29-Nov-2016 0.4 : DB version is now read after Login. Using Mechanize.
# *  28-Nov-2016 0.3 : Added ability to get DB version on the fly (File).
# *  25-Nov-2016 0.2 : Added Authentication capability.
# *                     URL now uses absolute path. 
# *                     Context loaded status is available.
# *  24-Nov-2016 0.1 : Created.
# *
# * CopyRight :
# * All rights reserved.
# **************************************************************/

require_relative './QA_Machines_API'
require_relative './Wiki_API'

require 'nokogiri'      # Parser
require 'mechanize'     # Login


$DEBUG_RUN = false
hline = "--------------"

# List of QA machines for local debugging
machines_list_local = 
{
    "16.2" => "./LAX1QAPP14.xhtml",
    "Trunk (17)" => "./lax1qapp05.xhtml",
}
if $DEBUG_RUN then printf("\n%1$s Debug run %1$s\n", hline) end

# Open file for writing
fname = get_output_filename()
file_out = File.open(fname, "w")

# Get the global authorization code
$JIRA_auth = get_auth_code("Requires JIRA credentials")

# And the authentication details
$AD_credentials = get_credentials("Please enter AD credentials")
$context_credentials = get_credentials("Please enter default context credentials")


# Get the list of machines to go through from the wiki page
wiki_base_url = "https://uplandsoftware.atlassian.net/wiki/rest/api"
id = 109609022 # PS QA Machines (ORD) = 109609022 # Test = 108789900
# Get the wiki page
printf("\n%s\n%s\n", hline, "Getting info from the wiki page")
page_details = get_wiki_page_content(wiki_base_url, id, $JIRA_auth)
page = page_details["page"]
if (page != "")
    
    content = Nokogiri::XML.fragment(page)

    #wiki_debug_write_file("file.xml", content)
    
    # There should be only one table
    table = content.css("table")
    
    # For every row, indicating a machine    
    rows = table.css("tr")    
    rows.each do |row|
        # For all columns
        # <version> <Server> <Home> <Build> <Contexts> <DB> <Java>
        #
        cols = row.css("td")
        if ( cols.length > 3 )
                
            url = cols[2].at_css("a").content
            
            # Get the latest details from PSHome #
            puts url
            machine_details = {}
            
            # Open the page
            if $DEBUG_RUN
                name = cols[0].content
                page = ""
                if machines_list_local.has_key?(name)
                    url = machines_list_local[name]
                    page = Nokogiri::HTML( open(url) )
                else
                    next
                end
            else
                page_info = handle_authentication(url)
                #p page_info
                if page_info["error"] == false
                    page = Nokogiri::HTML( page_info["stream"] )
                else
                    page = ""
                end
            end
            
            if page != ""
                # Get PSS version
                # The DIV with class headerRightValue contains the version information
                version = page.css("div.headerRightValue div")[1] # Second div element 
                #puts version.text
                
                # Get Java version
                java_version = page.css("div.headerRightValue div")[2] # Third div element
                #puts java_version.text
                
                # Get list of contexts with links
                contexts = get_contexts(page, url)
                #puts contexts
                
                # Get DB version
                db_version = get_context_db_version(contexts)
                #puts db_version

                # Write details to the file
                file_out.puts "\n"
                file_out.puts url
                
                file_out.puts version.text
                machine_details["version"] = version.text
                
                file_out.puts "Java = #{java_version.text}"
                machine_details["java"] = java_version.text
                
                file_out.puts "DB version = #{db_version}"
                machine_details["db"] = db_version.to_i
                
                context_buffer = []
                contexts.each do |name, details|
                    value = "[#{name} | #{details["context_url"]}]"
                    file_out.puts value
                    context_buffer.push(value)
                end
                machine_details["contexts"] = context_buffer
                file_out.puts "\n"
                
            else
                file_out.puts "\n"
                file_out.puts url
                file_out.puts page_info["stream"]
                file_out.puts "\n"
            end          
            
            
            # Update the vales only if valid
            if (machine_details.length > 0)
                # The source column can be blank or non-existent
                version = (cols.length >= 4) ? version = cols[3] : nil
                contexts = (cols.length >= 5) ? cols[4] : nil
                db_version = (cols.length >= 6) ? cols[5] : nil
                java_version = (cols.length >= 7) ? cols[6] : nil
                
                # The latest value can be blank or unknown
                if version != nil
                    # \remarks Preserve the structure when changing the values.
                    # The text is usually wrapped in a <span> element. What we want
                    # is to change the content of that span.
                    version.child.content = machine_details["version"]
                end
                
                if db_version != nil && (machine_details["db"].to_i > 0)
                    db_version.content = machine_details["db"]
                end
                
                if java_version != nil
                    java_version.content = machine_details["java"]
                end
                
                if contexts != nil && machine_details["contexts"].length > 0 
                    # First clear the content
                    contexts.content = ""
                    # Add a new element as a child and thus preserve the structure
                    new_contexts = Nokogiri::XML.fragment( wiki_format_links( machine_details["contexts"] ) )
                    contexts.add_child(new_contexts)
                end
            
            end # Valid machine details
            
        end # Valid columns
        
    end # Every row
    
    #wiki_debug_write_file("file_out.xml", content)
    
    # Now update the wiki page with latest info
    printf("\n%s\n%s\n", hline, "Updating the wiki page")
    wiki_update_page_content(wiki_base_url, id, $JIRA_auth, page_details["title"], content, page_details["page_version"])

end # Valid page

# Clean up
file_out.close

puts "\n*** Done ***\n"

# End of file #