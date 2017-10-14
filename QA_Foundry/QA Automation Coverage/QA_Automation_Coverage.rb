# /*************************************************************
# * PSS QA - Automation Coverage
# * Calculate the coverage data - Automation percent complete 
# * status for TestRail test suites.
# *
# * Version : 1.3
# * Changes :
# *
# *  19-Aug-2017 1.3 : The output is now in Markdown format.
# *  12-Aug-2017 1.2 : The data is now also output to file.
# *  04-Aug-2017 1.1 : Automation status is now calculated.
# *  28-Jul-2017 1.0 : Created.
# *
# * CopyRight :
# * All rights reserved.
# **************************************************************/

require_relative './../QA_Functions/QA_Lib_Io'
require_relative './../QA_Functions/QA_Lib_Net'

require 'pry'

$DEBUG_RUN = false


def calculate_automation_status(file_out)
    project_id = 1
    auth_code = get_auth_code("TR")
    base_url = "https://upland.testrail.com/index.php?/api/v2/"
    commands = [
        ["get_suites/", {} ],
        ["get_cases/", {:suite_id => 0} ],
    ]
    headers = {
                :Authorization => "Basic #{auth_code}",
                :content_type => "application/json",
    } 
    
    total_test_cases = 0
    total_automated_cases = 0
    
    print_log("\t Fetching all the test suites . . .\n", file_out)
    
    # First get all the test suites
    command_index = 0
    command = "#{base_url}#{commands[command_index][0]}#{project_id}"
    params = commands[command_index][1]
    headers[:params] = params
    
    list = perform_get(command, headers)
    
    print_log("\t\t Found #{list.length} suites\n\n", file_out)
    
    # For every test suite, get all the test cases under it
    # test suite, total, total automated
    suite_data = []
    list.each do |test_suite|
        suite_id = test_suite["id"]
        
        print_log("\t Working on #{test_suite["name"]}\n", file_out)
        
        # Get the cases
        command_index = 1
        command = "#{base_url}#{commands[command_index][0]}#{project_id}"
        params = commands[command_index][1]
        params[:suite_id] = suite_id
        headers[:params] = params
    
        list_cases = perform_get(command, headers)
        
        # Calculate the automation status
        total_automated = 0
        total_cases = list_cases.length
        list_cases.each do |test_case|
            if test_case["custom_isautomated"] == true
                total_automated += 1
            end
        end
        
        data = Hash.new
        data["id"] = suite_id
        data["name"] = test_suite["name"]
        data["total"] = total_cases
        data["automated"] = total_automated
        suite_data.push(data)

        total_test_cases += total_cases
        total_automated_cases += total_automated
        
    end
    
    print_log("\n\n", file_out)
    
    # Output in the Markdown syntax
    #
    # Two space at the end will add a line break.
    #
    # For the table - 
    # :-- Left
    # --: Right
    # :--: Center
    #
    print_log("| Suite | Automated | Total | Percent |  \n", file_out)
    print_log("| :---- | --------: | ----: | :-----: |  \n", file_out)
    
    suite_data.each do |details|
        automated = details["automated"]
        total = details["total"]
        percent = automated.to_f / total * 100
        if total > 0 then percent = percent.floor else percent = 0 end
        
        print_log("| #{details["name"]} | #{automated} | #{total} | #{percent}% |  \n", file_out)
    end
    
    print_log("\n----\n", file_out)
    
    total_percent = total_automated_cases.to_f / total_test_cases * 100
    if total_test_cases > 0 then total_percent = total_percent.floor else total_percent = 0 end
    print_log("Total #{total_automated_cases} cases automated out of #{total_test_cases} => #{total_percent}%\n", file_out)
    
end

start_time = Time.now

# Open the output log file
file_out = File.open("automation_coverage.md", "w")

# Get the automation status for each suite in TestRail
print_log("\n# Calculating automation coverage #\n", file_out)
calculate_automation_status(file_out)

end_time = Time.now
print_log("\nTotal time taken = #{end_time - start_time}s\n")

# Clean up
file_out.close

print_log("\n*** Done ***\n")

# End of file

