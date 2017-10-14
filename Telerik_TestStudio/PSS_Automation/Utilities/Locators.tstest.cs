using Telerik.TestingFramework.Controls.KendoUI;
using Telerik.WebAii.Controls.Html;
using Telerik.WebAii.Controls.Xaml;
using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;

using ArtOfTest.Common.UnitTesting;
using ArtOfTest.WebAii.Core;
using ArtOfTest.WebAii.Controls.HtmlControls;
using ArtOfTest.WebAii.Controls.HtmlControls.HtmlAsserts;
using ArtOfTest.WebAii.Design;
using ArtOfTest.WebAii.Design.Execution;
using ArtOfTest.WebAii.ObjectModel;
using ArtOfTest.WebAii.Silverlight;
using ArtOfTest.WebAii.Silverlight.UI;

namespace PSS_Automation
{

        public class Locators : BaseWebAiiTest
        {
        #region [ Dynamic Pages Reference ]

        private Pages _pages;

      
        public Pages Pages
        {
            get
            {
                if (_pages == null)
                {
                    _pages = new Pages(Manager.Current);
                }
                return _pages;
            }
        }

        #endregion
        
         
            
    
        [CodedStep(@"New Coded Step")]
        public void Locators_CodedStep()
        {
           
        }
        
    }
    
    public static class AppLocators {
        
        // Add your locators here...
        public static Dictionary<string, string> pssLocators = new Dictionary<string, string>()
        {
            {"projcentral_leftnav_plus_span","//div[@title='Project Central']//span[contains(@class,'ps-ext-plus-circle')]"},
            
            {"projcentral_leftnav_minus_span","//div[@title='Project Central']//span[contains(@class,'ps-ext-minus-circle')]"},
            
            {"financials_leftnav_plus_span","//div[@title='Financials']//span[contains(@class,'ps-ext-plus-circle')]"},
            
            {"financials_leftnav_minus_span","//div[@title='Financials']//span[contains(@class,'ps-ext-minus-circle')]"},
            
            {"measures_leftnav_plus_span","//div[@title='Measures']//span[contains(@class,'ps-ext-plus-circle')]"},
            
            {"measures_leftnav_minus_span","//div[@title='Measures']//span[contains(@class,'ps-ext-minus-circle')]"},
            
            {"references_leftnav_plus_span","//div[@title='References']//span[contains(@class,'ps-ext-plus-circle')]"},
            
            {"references_leftnav_minus_span","//div[@title='References']//span[contains(@class,'ps-ext-minus-circle')]"},
            
            {"more_leftnav_plus_span","//div[@title='More']//span[contains(@class,'ps-ext-plus-circle')]"},
            
            {"more_leftnav_minus_span","//div[@title='More']//span[contains(@class,'ps-ext-minus-circle')]"},
            
            {"report_leftnav_plus_span","//div[@title='Report']//span[contains(@class,'ps-ext-plus-circle')]"},
            
            {"report_leftnav_minus_span","//div[@title='Report']//span[contains(@class,'ps-ext-minus-circle')]"},
            
            {"resource_review_leftnav_plus_span","//div[@id='resource_review']//span[contains(@class,'ps-ext-plus-circle')]"},
            
            {"resource_review_leftnav_minus_span","//div[@id='resource_review']//span[contains(@class,'ps-ext-minus-circle')]"},
            
            {"reports_leftnav_plus_span","//div[@title='Reports']//span[contains(@class,'ps-ext-plus-circle')]"},
            
            {"reports_leftnav_minus_span","//div[@title='Reports']//span[contains(@class,'ps-ext-minus-circle')]"},
            
            {"usermanagement_leftnav_plus_span","//div[@title='User Management']//span[contains(@class,'ps-ext-plus-circle')]"},
            
            {"usermanagement_leftnav_minus_span","//div[@title='User Management']//span[contains(@class,'ps-ext-minus-circle')]"},
            
            {"importexport_leftnav_plus_span","//div[@title='Import/Export']//span[contains(@class,'ps-ext-plus-circle')]"},
            
            {"importexport_leftnav_minus_span","//div[@title='Import/Export']//span[contains(@class,'ps-ext-minus-circle')]"},
            
            {"templates_leftnav_plus_span","//div[@title='Templates']//span[contains(@class,'ps-ext-plus-circle')]"},
            
            {"templates_leftnav_minus_span","//div[@title='Templates']//span[contains(@class,'ps-ext-minus-circle')]"},
            
            {"layouts_leftnav_plus_span","//div[@title='Layouts']//span[contains(@class,'ps-ext-plus-circle')]"},
            
            {"layouts_leftnav_minus_span","//div[@title='Layouts']//span[contains(@class,'ps-ext-minus-circle')]"},
            
            {"permissions_leftnav_plus_span","//div[@title='Permissions']//span[contains(@class,'ps-ext-plus-circle')]"},
            
            {"permissions_leftnav_minus_span","//div[@title='Permissions']//span[contains(@class,'ps-ext-minus-circle')]"},
            
            {"localization_leftnav_plus_span","//div[@title='Localization']//span[contains(@class,'ps-ext-plus-circle')]"},
            
            {"localization_leftnav_minus_span","//div[@title='Localization']//span[contains(@class,'ps-ext-minus-circle')]"},
            
            {"configuration_leftnav_plus_span","//div[@title='Configuration']//span[contains(@class,'ps-ext-plus-circle')]"},
            
            {"configuration_leftnav_minus_span","//div[@title='Configuration']//span[contains(@class,'ps-ext-minus-circle')]"},
                                                
            {"username_top_div","//div[@class='userNameFormat']"},
            
            {"rev_dash_project_list_div","//div[contains(@class,'project_name')]/div[@class='page']/div"},
            
            {"rev_dash_projected_end_date_field_div","//div[contains(@class,'_cfs')]//div[@class='treeChildrenContent']/div[@class='page']/div[{0}]//div[@class='cellContent']"},
            
            {"rev_dash_editable_date_field_input", "//input[@id='ps_form_DatePicker_0']"},
            
            {"rev_dash_status_field_div","//div[contains(@class,'status')]//div[@class='page']/div[{0}]//div[@class='cellContent']"},
            
            {"rev_dash_new_status_icon_img","//table[starts-with(@id,'ps_ui_MenuBase_')]/tbody/tr[contains(.,'{0}')]//img"},
            
            {"rev_dash_scrollable_div", "//div[contains(@class,'workTree')]"},
            
            {"proj_plan_first_level_tree_div","//div[contains(@class,'workTree')]//div[@level='1' and contains(@class,'treeChildrenContent')]"},
            
            {"proj_plan_tree_parent_node_div","//div[contains(@class,'workTree')]//div[@level='{0}']/div/div/div[contains(@class,'dijitTreeExpando') and contains(.,'{1}')]"}, 
             
            {"proj_plan_next_tree_div","//div[contains(@class,'workTree')]//div[@level='{0}']/div/div/div[contains(@class,'dijitTreeExpando') and contains(.,'{1}')]/following-sibling::div[@class='treeChildrenContent']"},
        
            {"proj_plan_node_list_div","//div[contains(@class,'workTree')]//div[@containerid='{0}' and @level='{1}']/div[@class='page']/div"},
            
            {"proj_plan_editable_date_field_input","//input[@id='ps_form_DatePicker_0']"},
            
            {"proj_plan_act_start_date_field_div","//div[contains(@class,'actual_start_date')]//div[@containerid='{0}' and @level='{1}']/div[@class='page']/div[{2}]//div[@class='cellContent']"},
            
            {"proj_plan_act_end_date_field_div","//div[contains(@class,'actual_end_date')]//div[@containerid='{0}' and @level='{1}']/div[@class='page']/div[{2}]//div[@class='cellContent']"},
            
            {"proj_plan_pland_start_date_field_div","//div[contains(@class,'planned_start_date')]//div[@containerid='{0}' and @level='{1}']/div[@class='page']/div[{2}]//div[@class='cellContent']"},
            
            {"proj_plan_pland_end_date_field_div","//div[contains(@class,'planned_end_date')]//div[@containerid='{0}' and @level='{1}']/div[@class='page']/div[{2}]//div[@class='cellContent']"},
            
            {"proj_plan_sched_start_date_field_div","//div[contains(@class,'system_start_date')]//div[@containerid='{0}' and @level='{1}']/div[@class='page']/div[{2}]//div[@class='cellContent']"},
            
            {"proj_plan_sched_end_date_field_div","//div[contains(@class,'system_end_date')]//div[@containerid='{0}' and @level='{1}']/div[@class='page']/div[{2}]//div[@class='cellContent']"},
            
            {"proj_plan_dep_start_range_div","//div[contains(@class,'numcol')]//div[@containerid='{0}' and @level='{1}']/div[@class='page']/div[1]//div[@class='cellContent']"},
            
            {"proj_plan_dep_end_range_div","//div[contains(@class,'numcol')]//div[@containerid='{0}' and @level='{1}']/div[@class='page']/div[{2}]//div[@class='cellContent']"},
            
            {"proj_plan_dep_field_div","//div[contains(@class,'dependency')]//div[@containerid='{0}' and @level='{1}']/div[@class='page']/div[{2}]//div[@class='cellContent']"},
            
            {"proj_plan_editable_dep_input","//div[@id='ps_ui_wbs_WBSDependenciesInput_0']//input"},
            
            {"proj_plan_duration_field_div","//div[contains(@class,'duration')]//div[@containerid='{0}' and @level='{1}']/div[@class='page']/div[{2}]//div[@class='cellContent']"},
            
            {"proj_plan_editable_duration_input","//input[@id='ps_form_NumberTextBox_0']"},
            
            {"proj_plan_perc_comp_field_div","//div[contains(@class,'percent_complete')]//div[@containerid='{0}' and @level='{1}']/div[@class='page']/div[{2}]//div[@class='cellContent']"},
            
            {"proj_plan_editable_perc_input","//input[@id='ps_form_NumberTextBox_0']"},
            
            {"proj_plan_status_field_div","//div[contains(@class,'status')]//div[@containerid='{0}' and @level='{1}']/div[@class='page']/div[{2}]//div[@class='cellContent']"},
        
            {"proj_plan_new_status_icon_img","//table[starts-with(@id,'ps_ui_MenuBase_')]/tbody/tr[contains(.,'{0}')]//img"},
            
            {"proj_plan_down_arrow_icon_img","//div[@class='column am lastColumn']//div[@containerid='{0}' and @level='{1}']/div[@class='page']/div[{2}]//img[@class='actArrow']"},
            
            {"proj_plan_intended_node_div","//div[contains(@class,'workTree')]//div[@containerid='{0}' and @level='{1}']/div[@class='page']/div[contains(.,'{2}')]"},
            
            {"proj_plan_prev_node_div","//div[contains(@class,'workTree')]//div[@containerid='{0}' and @level='{1}']/div[@class='page']/div[{2}]/div[contains(@class,'cell')]"},
            
            {"proj_plan_save_and_cont_popup_btn","//input[@value='Save and continue']"},
            
            {"proj_work_tree_node_div","//div[@class='treeNodeCnt' and contains(.,'{0}')]"},
            
            {"proj_work_tree_node_plus_icon","//span[contains(@class,'dijitTreeExpandoClosed')]"},
            
            {"proj_work_tree_node_close_icon","//span[contains(@class,'dijitTreeExpandoOpened')]"},
            
            {"small_loader_image","//div[@id='qs_loading_img']//img"},
            
            {"Template_leftnav_plus_span","//div[@title='Templates']//span[@class='level ps-ext-plus-circle']"},
            
            {"Template_financial_div","//div[@id='ps_metric_template']"},
            
            {"Favorites_menu_item_div","//a[@class='js-menu-item']/div[contains(.,'{0}')]"},
            
            {"left_nav_pinned_menu_item", "//div[@id='PinContent']//a[contains(.,'{0}')]"},
           
            {"manage_fav_grid_name_items","//div[contains(@class,'namecol')]/div/div"},
            
            {"manage_fav_grid_desc_items","//div[contains(@class,'descrcol')]/div/div"},
            
            {"manage_fav_grid_url_items","//div[contains(@class,'urlcol')]/div/div"},
            
            {"manage_fav_grid_delete_icon_items","//div[contains(@class,'delcol')]/div/div"},
            
            {"manage_fav_any_pin_col_checkbox","//div[@class='cell']//input[@type='checkbox']"},
            
            {"myinbox_records_rows","//table[@id='PSTable']/tbody/tr"},
            
            {"create_other_work_location_search_result_div","//div[@id='resultsDiv']//li[contains(.,'{0}')]/div"},
            
            {"header_user_profile_link","//div[@id='popupUserSettingsWindow']/a[contains(.,'Profile')]"},
            
            {"bulid_number_div","//div[@class='loginBottom']//tr[1]//div[2]"},
            
            {"Where_am_i_icon_lastrow","//ul[@class='whereAmI']//li[not(descendant::ul)]//a"},
            
            {"Where_am_i_icon_firstrow","//ul[@class='whereAmI']/li/a"},
            
            {"Help_Manage_Link_Div","//td[@id='ps_ui_MenuItem_7_text']/a/div"},
            
            {"pagination_span","//span[@class='paginationMsg']"},
            
            {"project_manage_metrics_table_row","//form[@id='metricsForm']//table//tr"},
            
            {"project_manage_metrics_table_name_link","//td[@class='nameColumnValue']/a"},
            
            {"project_manage_metrics_table_delete_chkbx","//td[@class='deleteMetricColumnValue last']//input"},
            
            {"left_nav_menu_item_link_div","//div[@id='MenuContent']//a[@class='js-menu-item']//div[@title='{0}']"},                                
            
            {"metric_auto_work_type_checkbox","//tr[@valign='middle' and contains(.,'{0}')]//input"},
            
            {"metric_view_tab_tmpl_name_input","//table[@class='bgEdit']//tr[{0}]/td[1]/input"},
            
            {"metric_view_tab_tmpl_seq_input","//table[@class='bgEdit']//tr[{0}]/td[2]/input"},
            
            {"metric_view_tab_tmpl_cost_map_select","//table[@class='bgEdit']//tr[{0}]/td[3]/select"},
            
            {"metric_view_tab_tmpl_work_type_select","//table[@class='bgEdit']//tr[{0}]/td[4]/select"},
            
            {"metric_view_tab_cost_rollup_yes","//table[@class='bgEdit']//tr[{0}]/td[5]/input[1]"},
            
            {"metric_view_tab_edit_cost_rollup_yes","//table[@class='bgEdit']//tr[2]/td[5]/input[1]"},
            
            {"metric_view_tab_cost_rollup_no","//table[@class='bgEdit']//tr[{0}]/td[5]/input[2]"},
            
            {"metric_view_tab_edit_cost_rollup_no","//table[@class='bgEdit']//tr[2]/td[5]/input[2]"},
            
            {"metric_view_tab_cost_rollup_work_type_select","//table[@class='bgEdit']//tr[{0}]/td[6]//select"},
            
            {"metric_view_tab_edit_cost_rollup_work_type_select","//table[@class='bgEdit']//tr[2]/td[6]//select"},            
            
            {"metric_line_item_seq_input","//input[@name='Sequence.new{0}']"},
            
            {"metric_line_item_edit_seq_input","//table[@class='bgEdit']/tbody/tr/td/table[2]//tr[2]/td[1]/input"},
            
            {"metric_line_item_data_type_select","//select[@name='datatype.new{0}']"},
            
            {"metric_line_item_edit_data_type_select","//table[@class='bgEdit']/tbody/tr/td/table[2]//tr[2]/td[2]/select"},
            
            {"metric_line_item_name_input","//input[@name='ItemName.new{0}']"},
            
            {"metric_line_item_edit_name_input","//table[@class='bgEdit']/tbody/tr/td/table[2]//tr[2]/td[3]/input[5]"},
            
            {"metric_line_item_constraint_select","//select[@name='measurement-constraint.new{0}']"},
            
            {"metric_line_item_edit_constraint_select","//table[@class='bgEdit']/tbody/tr/td/table[2]//tr[2]/td[4]//select"},
            
            {"metric_line_item_behavior_select","//select[@name='behavior.new{0}']"},
            
            {"metric_line_item_edit_behavior_select","//table[@class='bgEdit']//table[2]//tr[2]/td[5]//select"},
            
            {"metric_line_item_default_input","//input[@name='default.new{0}']"},
            
            {"metric_line_item_edit_default_input","//table[@class='bgEdit']//table[2]//tr[2]/td[6]//input"},
            
            {"metric_line_item_scale_select","//select[@name='com.cinteractive.ps3.contexts.NumberScales.scales.new{0}']"}, 
            
            {"metric_line_item_edit_scale_select","//table[@class='bgEdit']//table[2]//tr[2]/td[7]//select"},            
            
            {"metric_line_item_rollup_checkbox","//input[@name='RollsUp.new{0}']"},
            
            {"metric_line_item_edit_rollup_checkbox","//table[@class='bgEdit']//table[2]//tr[2]/td[8]//input"},
            
            {"metric_line_item_description_input","//input[@name='Description.new{0}']"},            
            
            {"metric_line_item_edit_description_input","//input[contains(@name,'Description')]"},
             
            {"metric_static_value_input","//tr[contains(.,'{0}')]/td/input[@type='text']"},
            
            {"metric_range_value_min_input","//tr[contains(.,'{0}')]/following-sibling::tr[1]//input[contains(@name,'range-min')]"},            
            
            {"metric_range_value_max_input","//tr[contains(.,'{0}')]/following-sibling::tr[1]//input[contains(@name,'range-max')]"},            
            
            {"metric_range_value_default_input","//tr[contains(.,'{0}')]/following-sibling::tr[1]//input[contains(@name,'range-def')]"},            
            
            {"metric_set_seq_input","//tr[contains(.,'{0}')]/following-sibling::tr[{1}]/td[2]/input[1]"},            
            
            {"metric_set_value_input","//tr[contains(.,'{0}')]/following-sibling::tr[{1}]/td[2]/input[2]"},            
            
            {"metric_set_label_input","//tr[contains(.,'{0}')]/following-sibling::tr[{1}]/td[3]/input"},            
            
            {"metric_set_default_radio_input","//tr[contains(.,'{0}')]/following-sibling::tr[{1}]/td[4]/input"},   
            
            {"metric_computation_line_item_link","//span[@class='titleBoldLower']/a[contains(.,'{0}')]"},            
            
            {"metric_computation_formula_select","//tr[{0}]/td/select[@class='titleLower']"},          
            
            {"metric_open_first_template","//div[2]//span[@class='titleBoldlower']/a"},
            
            {"metric_first_row_history","//form[@name='main']/table[2]//tr[2]/td[2]"},

            {"metric_select_process_radio_button","//input[@value='Software Process']"},
            
            {"metric_instance_view_link","//li[@class='inactive' and contains(.,'{0}')]//a"},
           
            {"metric_instance_copy_view_item_link","//table[contains(@class,'dijitMenuTable')]//tr[contains(.,'{0}')]"},
            
            {"branding_header_logo_change_mark","//td[@id='headerLogoChangeMark' and contains(.,'*')]"},
            
            {"branding_header_logo_hcon_view_change_mark","//td[@id='headerLogoHcChangeMark' and contains(.,'*')]"},
            
            {"branding_print_logo_change_mark","//td[@id='printLogoChangeMark' and contains(.,'*')]"},
            
            {"branding_header_logo_size_error_cell","//td[@id='headerLogoError']"},
            
            {"branding_header_logo_hcon_size_error_cell","//td[@id='headerLogoHcError']"},
            
            {"branding_header_logo_hcon_error_table_cell","//td[@id='headerLogoHcError' and contains(.,'Invalid file format')]"},
            
            {"branding_h_logo_clear_image_table_cell","//td[@id='headerLogoPreviewEmpty' and contains(.,'Image coming soon')]"},
            
            {"branding_h_logo_hcon_clear_image_table_cell","//td[@id='headerLogoHcPreviewEmpty' and contains(.,'Image coming soon')]"},
            
            {"branding_clear_image_h_logo_hcon_table_cell","//td[@id='headerLogoHcPreviewEmpty' and contains(.,'Image coming soon')]"},
            
            {"branding_clear_image_print_logo_table_cell","//td[@id='printLogoPreviewEmpty' and contains(.,'Image coming soon')]"},
            
            {"branding_default_logo_Img","//img[@title='PowerSteering Software QA Test']"},
            
            {"brandind_default_logo_span","//span[@class='upland-pss no-print' and @title='PowerSteering Software QA Test']"},
            
            {"branding_print_logo_invalid_file_error_msg","//td[@id='printLogoError']"},
            
            {"branding_print_logo_size_error_msg","//td[@id='printLogoError']"},
            
            {"branding_login_logo_clear_image_td_cell","//td[@id='loginLogoPreviewEmpty' and contains(.,'Image coming soon')]"},
            
            {"branding_login_logo_star_mark","//td[@id='loginLogoChangeMark']"},
            
            {"branding_login_logo_invalid_file_error_msg","//td[@id='loginLogoError' and contains(.,'Invalid file format')]"},
            
            {"branding_login_logo_size_error_msg","//td[@id='loginLogoError']"},
            
            {"branding_email_logo_clear_image_td_cell","//td[@id='emailLogoPreviewEmpty' and contains(.,'Image coming soon')]"},
            
            {"branding_email_logo_star_mark","//td[@id='emailLogoChangeMark']"},
            
            {"branding_email_logo_invalid_file_error_msg","//td[@id='emailLogoError']"},
            
            {"branding_email_logo_size_error_msg","//td[@id='emailLogoError']"},
            
            {"branding_email_logo_background_color_select","//div[@class='psColorPaletteInner']//span/img[contains(@style,'{0}')]"},
            
            {"branding_email_logo_background_color_view","//div[@id='emailHeaderBackgroundColor']/div[2]"},
            
            {"branding_email_logo_hex_dec_val_input","//input[@id='emailHeaderBackgroundColor']"},
            
            {"branding_report_logo_clear_img_td_cell","//td[@id='reportLogoPreviewEmpty']"},
            
            {"branding_report_logo_star_mark","//td[@id='reportLogoChangeMark']"},
            
            {"branding_report_logo_invalid_file_error_msg","//td[@id='reportLogoError']"},
            
            {"branding_color_scheme_star_mark","//div[@id='colorSchemeChangeMark']"},
            
            {"branding_select_color_from_pallete_box","//div[@class='psColorPaletteInner']//span/img[contains(@style,'{0}')]"},
            
            {"branding_color_scheme_top_nav_txt_icon","//div[@id='cslTopNavLogo']/div"},
            
            {"branding_color_scheme_top_nav_icon_high","//div[@id='cslTopNavIcons']//li"},
            
            {"branding_color_scheme_left_nav_bkgd_color_left","//div[@id='cslLeftNav']"},
            
            {"branding_color_scheme_left_nav_bkgd_color_right","//div[@id='cslLeftNavMenu']"},
            
            {"branding_color_scheme_left_nav_txt_icon_color_left","//div[@id='cslLeftNav']//li"},
            
            {"branding_color_scheme_left_nav_txt_icon_color_right","//div[@id='cslLeftNavMenuText']//div"},
            
            {"branding_color_scheme_content_bkgd_color","//div[@id='cslContent']"},
            
            {"branding_color_scheme_content_txt_color","//div[@id='cslContentText']//div"},
            
            {"branding_color_scheme_top_nav_bkgd_color_view","//div[@id='PSColorPicker']/div[2]"},
                       
            {"branding_color_scheme_check_all_color_view","//div[@id='PSColorPicker_{0}']/div[2]"},
            
            {"branding_color_scheme_tnb_hex_dec_val_input","//input[@id='PSColorPicker']"},
            
            {"branding_color_scheme_check_all_hex_dec_val_input","//input[@id='PSColorPicker_{0}']"},
            
            {"branding_color_scheme_content_bckg_color_div","//div[@id='PageContent']"},
            
            {"branding_color_scheme_content_txt","//h2[contains(.,'Company logos')]"},
            
            {"top_nav_background_color_div","//div[@class='header']"},
            
            {"top_nav_background_txt_and_icon_color_div","//div[@class='pageTitleWrap']/div[1]"},
            
            {"left_nav_background_color_div","//div[@id='NavBar']"},
            
            {"login_page_logo","//img[@class='no-print']"},
            
            {"branding_login_lic_msg_starmark","//div[@id='licenseMessageChangeMark']"},
            
            {"branding_empty_txt_warning_msg","//div[@class='MsgBox RedBoxBorder']"},
            
            {"login_licence_pop_up_txt_div","//div[@class='dlgLoginMessage']/div[1]"},
            
            {"Custom_text_in_submit_help_desk_req","//div[@class='TopBorder']"},
            
            {"Custom_text_in_help_req_msg","//div[@class='TopBorder']"},
            
            {"uploaded_ppt_file_del_span","//tr[contains(.,'Automation')]//span"},
            
            {"branding_powerpoint_error_msg","//div[@class='MsgBox RedBoxBorder']"},
            
            {"branding_powerpoint_table_first_row","//table[@id='PSTable']//tr[2]"},
            
            {"branding_powerpoint_sort_asc_arrow_img","//img[contains(@src,'arrow-up.gif')]"},
            
            {"branding_powerpoint_sort_desc_arrow_img","//img[contains(@src,'arrow-down.gif')]"},
            
            {"admin_modulels_work_type_link", "//td[@class='titleBoldLower']//a[contains(.,'{0}')]"},
            
            {"admin_modules_met_search_res_select_link","//table[@class='bgDark']//table//tr[contains(.,'{0}')]//a"},
            
            {"admin_modules_met_view_radio","//input[@name='view']"},
            
            {"admin_module_met_line_item_checkbox","//input[@name='item']"},
            
            {"review_imp_exp_metric_looader_tab",".//*[@id='ps_metric_load']"},
            
            {"manage_history_datetime_records","//div[contains(@class,'workTree lastColumn')]/div/div"},
            
            {"manage_history_name_records","//div[@class='column name']/div/div"},
            
            {"manage_history_desc_records","//div[@class='column descr']/div/div"},
            
            {"manage_history_url_records","//div[@class='column url']/div/div"},
            
            {"manage_history_delete_icon_records","//div[@class='column action']/div/div//a[@title='Delete item']"},
            
            {"manage_history_add_to_fav_icon_records","//div[@class='column action']/div/div//a[@title='Add to favorites']"},
            
            {"manage_history_sort_dsc_img","//th[contains(.,'{0}')]//img[contains(@src,'header_sort_dsc.gif')]"},
            
            {"manage_history_sort_asc_img","//th[contains(.,'{0}')]//img[contains(@src,'header_sort_asc.gif')]"},
            
            {"status_report_templ_records","//table[@id='PSTable']//tr"},
            
            {"add_status_report_tmplt_work_item","//div[@class='dijitTreeNode' and contains(.,'{0}')]//input"},
            
            {"statur_report_table_record","//table[@id='PSTable']//tr[contains(.,'{0}')]"},
            
            {"statur_report_table_record_name_link","//table[@id='PSTable']//tr[contains(.,'{0}')]//td[@class='nameColumnValue']/div"},
            
            {"statur_report_table_record_name_icon","//table[@id='PSTable']//tr[contains(.,'{0}')]//td[@class='nameColumnValue']/img"},
            
            {"statur_report_table_record_edit_img","//table[@id='PSTable']//tr[contains(.,'{0}')]//td[@class='editColumnValue']//img"},
            
            {"statur_report_table_record_delete_btn","//td[contains(.,'Delete')]"},
            
            {"admin_log_rec_desc","//table[@id='PSTable']//tr/td[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}')]"},
            
            {"cust_fields_other_asso_col_status_rep_img", "//td[@class='otherTypesColumnValue']/img[contains(@title,'Status Report')]"},
            
            {"cust_fields_add_asso_status_repo_list", "//div[@class='dijitTreeNode']//img[@title='Status Report']/parent::label"},

            {"custom_fields_table_record","//table[@id='PSTable']//tr[contains(.,'{0}')]"},       

            {"custom_fields_record_name_link","//table[@id='PSTable']//tr[contains(.,'{0}')]//div[@class='link']"},       
            
            {"custom_field_record_other_association", "//table[@id='PSTable']//tr[contains(.,'{0}')]//td[@class='otherTypesColumnValue']//img"},
            
            {"custom_field_record_delete_icon", "//table[@id='PSTable']//tr[contains(.,'{0}')]//td[contains(@class,'deleteTagSetColumnValue')]//img"},
            
            {"home_page_config_table_record","//table[@id='PSTable']//tr[contains(.,'{0}')]"},       
            
            {"home_page_config_record_name_link","//table[@id='PSTable']//tr//td[@class='nameColumnValue' and contains(.,'{0}')]"},       
            
            {"home_page_config_records_name","//table[@id='PSTable']//td[@class='nameColumnValue']//a"},       
            
            {"home_page_config_record_desc", "//table[@id='PSTable']//tr[contains(.,'{0}')]//td[@class='descColumnValue']"},
            
            {"home_page_config_record_delete_icon", "//table[@id='PSTable']//tr[contains(.,'{0}')]//td[contains(@class,'deleteColumnValue')]//img"},
            
            {"home_page_config_record_last_editor", "//table[@id='PSTable']//tr[contains(.,'{0}')]//td[contains(@class,'editorColumnValue')]/div"},
            
            {"prefs_home_page_opts_record","//table[@id='PSTable']//tr[contains(.,'{0}')]"},
            
            {"prefs_home_page_drag_div","//table[@id='PSTable']//tr[contains(.,'{0}')]//td[@class='dndColumnValue']/div"},
            
            {"prefs_home_page_record_chkbx","//table[@id='PSTable']//tr[contains(.,'{0}')]//input"},
            
            {"new_hmc_config_opts_record","//table[@id='PSTable']//tr[contains(.,'{0}')]"},
            
            {"new_hmc_config_drag_div","//table[@id='PSTable']//tr[contains(.,'{0}')]//td[@class='dndColumnValue']/div"},
            
            {"new_hmc_config_record_chkbx","//table[@id='PSTable']//tr[contains(.,'{0}')]//input"},
            
            {"dash_layout_page_records_name","//table[@id='PSTable']//td[@class='nameColumnValue']"},       
            
            {"exrev_layout_page_records_name","//table[@id='PSTable']//td[@class='nameColumnValue']"},       
            
            {"rev_portfolios_page_records_name","//table[@id='PSTable']//td[@class='nameColumnValue']//a[1]"},       
            
            {"home_page_module_item","//div[@id='content']//h2[contains(.,'{0}')]"},
            
            {"metric_line_item_delete_span"," //form[@name='main']//table//table[2]//tr[{0}]//span[@class='cancel']"},
            
            {"powerpoint_template_name_records","//table[@id='PSTable']/tbody/tr/td[1]/a"},
            
            {"user_name_link","//td[@class='nameColumnValue']/div[contains(.,'{0}')][1]"},
            
            {"user_profile_setting_warning_msg","//div[@class='MsgBox RedBoxBorder']"},
            
            {"User_profile_red_dot_img","//img[@id='EmailRequiredDot']"},
            
            {"admin_agent_name_link","//span[@class='titleBoldLower']/a[contains(.,'{0}')]"},
            
            {"public_report_subfolder_name_link","//div[@class='dijitTreeNode' and contains(.,'{0}')]//a"},
            
            {"column_best_practice_all_checkbox","//div[@id='Project / Best Practice']//div[contains(.,'All')]//input"},
            
            {"generated_report_div","//td[@class='nameColumnValue']//div[contains(.,'{0}')]"},
            
            {"output_result_report_name","//span[contains(.,'{0}')]"},
            
            {"report_resulted_project_name","//span[contains(.,'{0}')]"},
            
            {"report_wizard_select_user_name_div","//div[@class='link' and contains(.,'{0}')]"},
            
            {"report_chekbox","//tr[contains(.,'{0}')]//input"},
            
            {"html_report_project_name_row","//table[@class='jrPage']//tr[contains(.,'{0}')]"},
            
            {"html_report_aproved_by_user_name","//table[@class='jrPage']//tr[contains(.,'{0}')]//span[contains(.,'{1}')]"},
            
            {"html_report_approved_comment","//table[@class='jrPage']//tr[contains(.,'{0}')]//span[contains(.,'{1}')]"},
            
            {"html_report_approved_on_date","//table[@class='jrPage']//tr[contains(.,'{0}')]//span[contains(.,'{1}')]"},
            
            {"html_report_best_practice_status","//table[@class='jrPage']//tr[contains(.,'{0}')]//span[contains(.,'{1}')]"},
            
            {"report_wiz_best_prac_status_chkbox","//label[contains(.,'Best Practice status')]/preceding::input[1]"},
            
            {"report_wiz_set_nominee_chkbox","//label[contains(.,'Nominee')]/preceding::input[1]"},
            
            {"report_wiz_is_best_prac_chkbox","//div[@class='white clearfix']//tr[contains(.,'Is best practice')][1]//label[contains(.,'Is best practice')]/preceding::input[1]"},
            
            {"html_report_is_best_practice_bool","//table[@class='jrPage']//tr[contains(.,'{0}')]//span[contains(.,'{1}')]"},
            
            {"report_wiz_is_best_prac_nominee_chkbox","//label[contains(.,'Is best practice nominee')]/preceding::input[1]"},
            
            {"report_wiz_approved_by_chkbox","//label[contains(.,'Approved by')]/preceding::input[1]"},
            
            {"report_wiz_nominated_by_chkbox","//label[contains(.,'Nominated by')]/preceding::input[1]"},
            
            {"html_report_nominated_by_user_name","//table[@class='jrPage']//tr[contains(.,'{0}')]//span[contains(.,'{1}')]"},
            
            {"html_report_nominated_on_chkbox","//label[contains(.,'Nominated on')]/preceding::input[1]"},
            
            {"html_report_nominated_on_date","//table[@class='jrPage']//tr[contains(.,'{0}')]//span[contains(.,'{1}')]"},
            
            {"html_report_nomination_cmt_chkbox","//label[contains(.,'Nomination comment')]/preceding::input[1]"},
            
            {"html_report_nomination_comment","//table[@class='jrPage']//tr[contains(.,'{0}')]//span[contains(.,'{1}')]"},
            
            {"report_wiz_rejected_by_chkbox","//label[contains(.,'Rejected by')]/preceding::input[1]"},
            
            {"html_report_rejected_by_user_name","//table[@class='jrPage']//tr[contains(.,'{0}')]//span[contains(.,'{1}')]"},
            
            {"html_report_rejection_cmt_chkbox","//label[contains(.,'Rejected comment')]/preceding::input[1]"},
            
            {"html_report_rejected_comment","//table[@class='jrPage']//tr[contains(.,'{0}') and contains(.,'{1}')]"},
            
            {"report_wizard_set_option","//select[@id='criteria']"},
            
            {"html_report_rejected_on_chkbox","//label[contains(.,'Rejected on')]/preceding::input[1]"},
            
            {"html_report_rejected_on_date","//table[@class='jrPage']//tr[contains(.,'{0}')]//span[contains(.,'{1}')]"},
            
            {"report_wiz_designation_rem_by_chkbox","//label[contains(.,'Designation removed by')]/preceding::input[1]"},
            
            {"html_report_designation_rem_cmt_chkbox","//label[contains(.,'Designation removed comment')]/preceding::input[1]"},
            
            {"report_wiz_designation_rem_on_chkbox","//label[contains(.,'Designation removed on')]/preceding::input[1]"},
            
            {"work_status_change_link","//span[@class='titleBoldLower']/a[contains(.,'{0}')]"},
            
            {"status_report_link","//span[@class='titleBoldLower']/a[contains(.,'{0}')]"},
            
            {"logs_event_change_message_row","//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]"},
            
            {"add_new_tag_dailog_people_input","//input[@value='{0}']"},
            
            {"created_tag_row","//tr[contains(.,'{0}')]"},
            
            {"delete_custom_tag_image","//tr[contains(.,'{0}')]//img[@title='Remove Tag']"},
            
            {"tag_red_dot_img","//th[contains(.,'{0}')]/img"},
            
            {"created_custom_field_row","//tr[contains(.,'{0}')]"},
            
            {"Custom_field_red_dot_img","//table[@class='simple spaced']//th[contains(.,'{0}')]/img"},
            
            {"delete_custom_field_image","//tr[contains(.,'{0}')]//img[@title='Remove Field']"},
            
            {"field_management_edit_page_tag_visibility_link","//div[@id='dndArea']/div/div[contains(.,'{0}')]/preceding::div[1]"},
            
            {"tag_not_found_cell","//table[@class='simple spaced']//th[contains(.,'{0}')]"},
            
            {"custom_field_not_found_cell","//table[@class='simple spaced']//th[contains(.,'{0}')]"},
            
            {"field_management_edit_page_custom_field_visibility_link","//div[@id='dndArea']/div/div[contains(.,'{0}')]/preceding::div[1]"},
            
            {"edit_field_user_required_checkbox","//div[@id='dndArea']/div/div[contains(.,'{0}')]/preceding::div[6]/input"},
            
            {"edit_field_user_locked_checkbox","//div[@id='dndArea']/div/div[contains(.,'{0}')]/preceding::div[4]/input"},
            
            {"tag_locked_div","//table[@class='simple spaced']//tr[contains(.,'{0}')]/td/div"},
            
            {"custom_field_red_dot_img","//th[contains(.,'{0}')]/img"},
            
            {"custom_field_locked_div","//table[@class='simple spaced']//tr[contains(.,'{0}')]/td/div"},
            
            {"import_new_users_error_msg_div","//div[@class='MsgBox RedBoxBorder']"},
            
            {"created_custom_field_div","//tr//div[contains(.,'{0}')]"},
            
            {"generated_dashboard_layout_row","//tr[contains(.,'{0}')]"},
            
            {"generated_dashboard_layout_link","//tr//a[contains(.,'{0}')]"},
            
            {"dashboard_layout_show_more_less","//a[@id='paginateViewToggle']/img"},
            
            {"dashboard_custom_field_column","//th[contains(.,'{0}')]"},
            
            {"generated_dashboard_layout_checkbox","//tr[contains(.,'{0}')]//input"},
            
            {"select_work_item_for_custom_fields","//th[contains(.,'Work Types:')]/following-sibling::td/div"},
            
            {"column_custom_fields_all_checkbox","//div[@id='Project / Custom Fields']//div[contains(.,'All')]//input"},
            
            {"report_custom_field_column","//span[contains(.,'{0}')]"},
            
            {"create_idea_location_search_result_div","//ul[@id='div_location']/li[contains(.,'Idea Pipeline')]/div"},
            
            {"after_find_user_name_div","//ul[@id='div_owner']/li/div[contains(.,'{0}')]"},
            
            {"idea_hopper_name_link_row","//tr[contains(.,'{0}')]"},
            
            {"idea_hopper_del_checkbox","//tr[contains(.,'{0}')]//input"},
            
            {"submit_new_idea_page_span","//div[@id='sub']//span[contains(.,'{0}')]"},
            
            {"add_submit_idea_project_type_input","//div[@class='treeNodeCnt']//label[contains(.,'{0}')]/input"},
            
            {"add_submit_idea_department_checkbox","//li[contains(.,'{0}')]/input"},
            
            {"add_submit_idea_functional_group_chkbox","//li[contains(.,'{0}')]/input"},
            
            {"add_submit_idea_functional_grp_div","//tr[contains(.,'Functional Group')]//div[@class='MultiSelect left']"},
            
            {"add_submit_idea_location_checkbox","//div[@class='treeNodeCnt']/label[contains(.,'{0}')]/input"},
            
            {"add_submit_idea_strategic_alignment_div","//div[@class='dijitTreeComboNode']/div[contains(.,'{0}')]"},
            
            {"submited_idea_link","//a[contains(.,'{0}')]"},
            
            {"status_of_idea_summary_page_waiting_span","//span[@class='left' and contains(.,'Awaiting Approval')]"},
            
            {"status_of_idea_summary_page_approved_span","//form[@id='PSForm_3']/span[contains(.,'State:')]"},
            
            {"add_idea_hopper_set_loc","//select[@id='locType']/option[contains(.,'{0}')]"},
            
            {"select_work_location_search_result_div","//ul[@id='div_chooseWork']//div[contains(.,'{0}')]"},
            
            {"plus_sign_projects_formula_builder","//tr[contains(.,'Projects:')]//img"},
            
            {"setting_default_location_div","//ul[@id='div_DEFPARENT']//div[contains(.,'{0}')]"},
            
            {"idea_hopper_create_idea_project_type_div","//th[contains(.,'Project Type')]/following-sibling::td/div"},
            
            {"idea_hopper_create_idea_location_type_div","//th[contains(.,'Location')]/following-sibling::td/div"},
            
            {"idea_hopper_create_idea_stratagic_alignment_type_div","//th[contains(.,'Strategic Alignment')]/following-sibling::td/div"},
            
            {"project_summary_page_idea_link","//div[@id='DescendantsGridId']//a[contains(.,'{0}')]"},
            
            {"project_summary_page_parent_link","//th[contains(.,'Parent:')]/following-sibling::td/a"},
            
            {"search_icon_result_list_item","//ul[@id='qs_organizationSearchResults']/li[contains(.,'{0}')]"},
            
            {"import_new_users_error_handler_missing_grp_col_msg_div","//tr[contains(.,'{0}')]"},
            
            {"import_new_users_error_handler_missing_grp_col_val_msg_div","//tr[contains(.,'{0}')]"},
            
            {"import_new_users_error_handler_missing_def_parent_col_msg_div","//tr[contains(.,'{0}')]"},
            
            {"measure_library_name_link","//td[@class='titleColumnValue']/a[contains(.,'{0}')]"},
            
            {"measure_library_traingle_img","//td[contains(.,'{0}')]//img"},
            
            {"measure_library_offline_table_cell","//tr[contains(.,'X_measure_template01')]//td[@class='stateColumnValue last']"},
            
            {"quick_search_input","//ul[contains(@id,'SearchResults')]/li[contains(@title,'{0}')]"},
            
            {"manage_measures_on_summary_page_column_status","//tr[contains(.,'{0}')]//td[@class='displayColumnValue']"},
            
            {"manage_measures_from_library_column_status","//tr[contains(.,'{0}')]//td[@class='fromLibraryColumnValue last']"},
            
            {"add_measures_library_input","//tr[contains(.,'{0}')]//input"},
            
            {"project_measure_library_remove","//td[contains(@id,'ps_ui_MenuItem') and contains(.,'Remove')]"},
            
            {"project_measure_library_delete","//td[contains(@id,'ps_ui_MenuItem') and contains(.,'Delete')]"},
            
            {"view_measure_tab_div","//td[contains(@id,'ps_ui_MenuItem')]/a/div[contains(.,'View measure')]"},
            
            {"view_properties_tab_div","//td[contains(@id,'ps_ui_MenuItem')]/a/div[contains(.,'View properties')]"},
            
            {"project_measure_library_edit","//td[contains(@id,'ps_ui_MenuItem')]/a/div[contains(.,'Edit')]"},
            
            {"project_measure_description_column","//tr[contains(.,'{0}')]/td[@class='descriptionColumnValue']"},
            
            {"project_measure_library_copy","//td[contains(@id,'ps_ui_MenuItem')]/a/div[contains(.,'Copy')]"},
            
            {"project_measure_library_do_not_show_on_summary_page","//td[contains(@id,'ps_ui_MenuItem')]/a/div[contains(.,'Do not show on summary page')]"},
            
            {"project_measure_library_show_on_summary_page","//td[contains(@id,'ps_ui_MenuItem')]/a/div[contains(.,'Show on summary page')]"},
            
            {"login_page_locale_list","//div[@id='localeList']//div[contains(.,'{0}')]"},
            
            {"quick_search_result_bold_element","//ul[contains(@id,'SearchResults')]//*[contains(text(),'{0}')]"},
            
            {"quick_search_result_project_section","//tr[@id='qs_projectSearchResultsRow']//ul[contains(@id,'SearchResults')]/li[@title='{0}']"},
            
            {"quick_search_result_people_section","//ul[@id='qs_peopleSearchResults']/li[contains(@title,'{0}')]"},
            
            {"report_wizard_custom_project_checkbox","//label[contains(.,'Custom Projects')]/preceding-sibling::input"},
            
            {"report_wizard_work_template_checkbox","//label[contains(.,'Work Template')]/preceding-sibling::input"},
            
            {"report_wizard_created_by_checkbox","//label[contains(.,'Created by')]/preceding-sibling::input"},
            
            {"favorite_list","//div[@class='column editable namecol']/div/div"},
            
            {"delete_favorite_link_span","//div[@class='column delcol']/div/div[{0}]//span[@title='Delete item']"},
            
            {"added_metric_template_left_nav_menu","//div[contains(@id,'ps_metric') and @title='{0}']"},
            
            {"metric_template_link","//td[@id='contentCell']//a[contains(.,'{0}')]"},
            
            {"advanced_template_link","//a[contains(.,'{0}')]"},
            
            {"select_work_item_for_tags","//div[@id='workTypesDisp']"},
            
            {"tag_dependencies_down_arrow_img","//td[contains(.,'{0}')]//img"},
            
            {"tag_dependencies_delete_cell","//td[contains(@id,'ps_ui_MenuItem')][contains(.,'Delete')]"},
            
            {"add_project_searched_people_link","//li[contains(@id,'dojoUnique')]/a[contains(.,'{0}')]"},
            
            {"project_summary_team_members_div","//div/h4[contains(.,'Team Members')]/following-sibling::table//td/div[contains(.,'{0}')][@class='link']"},
            
            {"summary_page_parent_location_link","//div[@class='block clearfix summary']//th[contains(.,'Parent:')]/following-sibling::td/a[contains(.,'{0}')]"},
            
            {"inbox_notification_project_contains_div","//form[@id='Main']//div[@class='box'][contains(.,'{0}')]"},
            
            {"inbox_notification_mark_as_read_checkbox","//div[contains(.,'{0}')]//input[@id='Checkbox']"},
            
            {"inbox_notification_recent_history_row","//h4[contains(.,' Recent History')]/following-sibling::table//tr[contains(.,'{0}')]"},
            
            {"inbox_notification_recent_history_project_link","//div[@class='box']//a[contains(.,'{0}')]"},
            
            {"quick_search_project_result_list","//ul[@id='qs_projectSearchResults']/li[contains(.,'{0}')]"},
            
            {"quick_search_people_result_list","//ul[@id='qs_peopleSearchResults']/li[contains(.,'{0}')]"},
            
            {"quick_search_organization_result_list","//ul[@id='qs_organizationSearchResults']/li[contains(.,'{0}')]"},
 
       };
        
        
 
        
        public static string get(string key){
            
            
            if(pssLocators.ContainsKey(key)){
                return pssLocators[key];
            }
            else {
                throw new System.ArgumentException(string.Format("Locator '{0}' Not Found!!!", key));
                
            }
            
           
        }
    }
    
}
