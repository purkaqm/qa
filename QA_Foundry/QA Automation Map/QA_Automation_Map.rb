# /*************************************************************
# * PSS QA - Automation Map
# * Map the Test Studio tests with the corresponding TestRail test cases.
# *
# * Version : 1.1
# * Changes :
# *
# *  18-Aug-2017 1.1 : The output is now in Markdown format.
# *  12-Aug-2017 1.0 : Created.
# *
# * CopyRight :
# * All rights reserved.
# **************************************************************/

require_relative './../QA_Functions/QA_Lib_Io'
require 'json'

require 'pry'

$DEBUG_RUN = false

$test_files = [
    "\\AutomationTests\\ImportantLinks\\TST_IMPL_001.tstest",
    "\\AutomationTests\\Manual Acceptance Test\\TST_MAT_001_C992539.tstest",
    "\\AutomationTests\\NavigationAndUI\\TST_NAV_UI_001.tstest",
    "\\ApplicationLibrary\\Add\\TS_Create_Other_Work.tstest",
    "\\AutomationTests\\Measures Library\\TST_ML_023.tstest",
]

def extract_mapping_data(test_script_path)
    # The reference to the test case can be found sometimes in 
    # description and sometimes in test script name.    
    data = Hash.new
    
    if test_script_path != ""
        # Read the content of the test script file
        script_content = ""
        File.open(test_script_path, "r").each do |line|
            script_content += line
        end
        
        # Parse JSON
        script = JSON.parse(script_content)
        
        # Find description
        description = script["__value"]["Description"]
        name = script["__value"]["Name"]
        
        # Find the test case references
        test_cases = []
        if description != ""
            key = "C"
            index = description.index(key)
            case_ref = description.slice(index, description.length - index)
            case_ref = case_ref.strip
            case_ref = case_ref.gsub(",", " ")
            
            # Convert to array
            test_cases = case_ref.split(" ")            
        end
        
        # Test case reference could also be present in file name itself
        ref_in_name = name.split("_")
        ref_in_name.delete_if {|item| !item.start_with?('C')}
        test_cases.concat(ref_in_name)
        
        # Retain only the valid entries
        # Entry is a valid case id reference if - 
        # 1. It starts with C
        # 2. Has more than 2 characters
        # 3. The second character must be a number
        #
        test_cases.delete_if {|item| \
            !item.start_with?('C') || \
            item.length < 2 || \
            !(item[1] <= '9' && item[1] >= '0') } # Remove from array if not a case reference
        
        # Remove duplicates
        test_cases = test_cases.uniq
        
        data["script"] = name
        data["cases"] = test_cases        
    end
    
    return (data)
end

def extract_automation_map(project_path, file_out)
    # Recursively extract the mapping data 
    map_data = Hash.new
    
    if $DEBUG_RUN
        $test_files.each do |test_file|
            data = extract_mapping_data("#{project_path}#{test_file}")
            map_data[data["script"]] = data["cases"]
        end
    else
        # Get all TSTEST files, recursively from the root project path
        pwd = Dir.pwd
        Dir.chdir(project_path)
        files = Dir["**/*.tstest"]
        Dir.chdir(pwd)
        print_log("\n\tFound #{files.length} test script files\n\n")
        
        # Now extract the mapping data
        files.each do |file|
            test_file = File.path(file).gsub("/", "\\")
            print_log("  #{test_file}\n", file_out)
            data = extract_mapping_data("#{project_path}\\#{test_file}")
            map_data[data["script"]] = data["cases"]
        end
    end
    
    return (map_data)
end

def print_map_data(map_data, file_out)
    # Format the map data and save it to the output file
    #
    # The format should be something like this - 
    # <Script>  <Cases> <URLs>
    # 
    # <Cases> = <Case1, Case2, Case3 ...>
    # <URLS> = <URL1\nURL2\nURL3 ...>
    #
    case_url = "https://upland.testrail.com/index.php?/cases/view"
    
    print_log("\n\n", file_out)
    
    # For each test script
    map_data.each do |test_script, details|
        # If there are references to the cases
        if (details.length > 0)
            print_log("#{test_script}\n", file_out)
            
            # Print every reference
            details.each do |case_id|                
                case_id = case_id.gsub("C", "")
                url = "#{case_url}/#{case_id}"
                
                print_log(" [C#{case_id}](#{url}) / \n", file_out)
            end
            
            print_log("\n", file_out)
        end
        
    end
end

print_log("\n--------\n")

if (ARGV.length < 1)
    print_log("\nUsage: <script> <project_path>\n")
    exit()
elsif (ARGV[0] != "")
    project_path = ARGV[0]
    
    start_time = Time.now
    
    # Open the output log file
    file_out = File.open("automation_map.md", "w")
    
    print_log("# Extracting Automation Map # \n", file_out)
    
    # Extract the mapping data
    print_log("```\n", file_out) # Start the code block
    print_log("Root = \"#{project_path}\" \n", file_out)
    map_data = extract_automation_map(project_path, file_out)
    print_log("```\n", file_out) # End the code block
    print_map_data(map_data, file_out)
    
    # Clean up
    file_out.close
    
    end_time = Time.now
    print_log("\nTotal time taken = #{end_time - start_time}s\n")
    
    print_log("\n\n*** Done ***\n\n")
end

# End of file
