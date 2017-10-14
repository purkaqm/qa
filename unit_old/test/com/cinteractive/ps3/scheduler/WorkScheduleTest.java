package com.cinteractive.ps3.scheduler;

/* Copyright � 2000-2001 Cambridge Interactive, Inc. All rights reserved.*/

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import ps5.psapi.project.create.WorkTypeBean;
import ps5.support.Util;
import ps5.wbs.WBSTestUtils;
import ps5.wbs.WBSTestHelper;
import ps5.wbs.beans.BeansTree;
import ps5.wbs.beans.Dependency;
import ps5.wbs.beans.IndexedDependency;
import ps5.wbs.beans.ProjectControl;
import ps5.wbs.logic.CreateWorkBean;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.database.StringPersistentKey;
import com.cinteractive.ps3.calendar.CalendarBean;
import com.cinteractive.ps3.entities.Admins;
import com.cinteractive.ps3.entities.Group;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.relationships.DependencyType;
import com.cinteractive.ps3.scheduler.calendar.DateCalculator;
import com.cinteractive.ps3.scheduler.calendar.DateCalculatorImpl;
import com.cinteractive.ps3.scheduler.newproblems.CyclicDependencyProblem;
import com.cinteractive.ps3.scheduler.newproblems.DependencyConstraintProblem;
import com.cinteractive.ps3.scheduler.newproblems.FDConstraintProblem;
import com.cinteractive.ps3.scheduler.newproblems.FNLTConstraintProblem;
import com.cinteractive.ps3.session.PSSession;
import com.cinteractive.ps3.test.PSTestBase;
import com.cinteractive.ps3.work.MasterTask;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkStatus;
import com.cinteractive.ps3.work.WorkUtil;
import com.cinteractive.scheduler.Duration;

/** Test case of tags management.*/
public class WorkScheduleTest extends PSTestBase {
    private User user = null;
    private WorkScheduleTestUtils utils;
    static String userMail = "AUTO_GEN_USER_MAIL_4@test.test";
    private static int testId = 0;

    static boolean cleaned = false;
    public void setUp() {
        super.setUp();
        user = User.getByEmailAddress(userMail, getContext());
        if (user == null) {
            System.out.println("Creating new user");
            user = User.createNewUser(userMail, getContext());
            user.setTimeZoneId(TimeZone.getDefault().getID());
            user.save();
        }
        
        Group g = Admins.get(getContext());
        if (!g.isMember(user)) {
            g.addUser(user);
            g.setModified();
            g.save();
        }
        WBSTestHelper.init(WBSTestUtils.createVisitProxy(user));

        // !!!!
        // It changes a calendar!!!
//        ResCalendar resCalendar = getContext().getResCalendar().newCopy();
//        daysOff = resCalendar.getDaysOff();
//        List<Integer> days = new ArrayList<Integer>();
//        days.add(6);
//        days.add(7);
//        resCalendar.setDaysOff(days);
//        resCalendar.save(user);

        PSSession session = PSSession.createAutoSession(user);
        
        user = session.getPrincipal();
        user.setTimeZoneId(TimeZone.getDefault().getID());
        System.out.println(user);
        utils = WorkScheduleTestUtils.getInstance(getContext(), user, session);
        if (!cleaned) {
         //   utils.removeWorks();
            cleaned = true;
        }
        
        System.out.println(String.format("-------------------- %s (%d) --------------------", getName(), ++testId));
    }
    

    public void tearDown() {
     //   utils.removeWorks();
//        ResCalendar resCalendar = getContext().getResCalendar();
//        resCalendar.setDaysOff(daysOff);

        super.tearDown();
    }

    public WorkScheduleTest(String name) {
        super(name);
    }

    private CalendarBean getCalendar(Float ... hours) {
        List<Float> data = new ArrayList<Float>();
        data.addAll(Arrays.asList(hours));
        Collections.addAll(data, 0.f, 0.f, 0.f, 0.f, 0.f, 0.f, 0.f);
        CalendarBean calendar = new CalendarBean();
        calendar.setAvailability(100.0f);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMaxHours(40.0f);
        calendar.setMondayHours(data.get(0));
        calendar.setTuesdayHours(data.get(1));
        calendar.setWednesdayHours(data.get(2));
        calendar.setThursdayHours(data.get(3));
        calendar.setFridayHours(data.get(4));
        calendar.setSaturdayHours(data.get(5));
        calendar.setSundayHours(data.get(6));
        return calendar;
    }
    
    public void test80564() {
    	// we shouldn't throw a problem detection for the "w2" item even if the item cannot be scheduled.
    	// dependency is property of "w3" and should affect on its schedule only.
        utils.run(new TestWork[]{
                new TestWork("w1", null, Constraint.ASAP, null,   5, "05.21.12", "05.25.12", null),
                new TestWork("w2", "w1", Constraint.ASAP, null,   3, "05.21.12", "05.23.12", null),
       TestWork.createFDWork("w3", "w1", Constraint.FD,  "05.22.12", "05.25.12", null, "05.22.12", "05.25.12", "w2")
            }
        );
        utils.run(new TestWork[]{
        		new TestWork("w1", null, Constraint.ASAP, null,   5, "05.22.12", "05.30.12", null),
        		new TestWork("w2", "w1", Constraint.ASAP, null,   3, "05.28.12", "05.30.12", "w3"),
        		TestWork.createFDWork("w3", "w1", Constraint.FD,  "05.22.12", "05.25.12", null, "05.22.12", "05.25.12", null)
        	}
        );
        new ThrowExceptionTest("w2", DependencyConstraintProblem.class, "w3") {
        	void invoke() {
		        utils.run(new TestWork[]{
		        		new TestWork("w1", null, Constraint.ASAP, null,   5, "05.22.12", "05.30.12", null),
		        		new TestWork("w2", "w1", Constraint.FNLT, "05.29.12", 3, "05.27.12", "05.29.12", "w3"),
		        		TestWork.createFDWork("w3", "w1", Constraint.FD,  "05.22.12", "05.25.12", null, "05.22.12", "05.25.12", null)
		        });
        	}
        };
        new ThrowExceptionTest("w3", DependencyConstraintProblem.class, "w4") {
        	void invoke() {
        		utils.run(new TestWork[] {
        				new TestWork("w1",null,     Constraint.ASAP,"01.01.07",null,    "11.20.07", "12.14.2007",  null),
        				new TestWork("w2","w1",     Constraint.SNET,"11.20.07",null,    "11.20.07", "12.14.2007",  null),
        				new TestWork("w3","w2",     Constraint.ASAP,"12.14.2007",10,    "11.20.07", "12.03.2007",  null),
        				new TestWork("w4","w2",     Constraint.FNLT,"12.14.2007",10,    "12.03.07", "12.14.2007", "w3"),
        		});
        	}
        };
    }
    
    public void test69048() {
        final PersistentKey id = new StringPersistentKey("o2k0k480000ho10rdgmg000000");
Nobody.get(getContext());
//User.hg+
//PSPrincipal p = new PSP

//User.setCurrentPrincipal((User)User.getUser(new StringPersistentKey("18001ug0000fj1fru2sg000000"), getContext()));
//        for(int i = 0; i < 2; ++i) {
//            new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        System.out.println("!!!!");
//                        for(int i = 0; i < 1000; ++ i) {
//                            Work work = (Work) Work.get(id, getContext());
//                        
//                            Timestamp tm = new Timestamp((long)(Math.random() * 1000000000));
//                            work.getSchedules().setSystemStartDate(tm);
//                            work.getSchedules().setSystemEndDate(tm);
//                            work.save();
//                        }
//                        
//                    } catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    
//                    System.out.println("!!!!-ENDDDDDDDDDDDDd");
//                }
//            }.run();
//        }
//  //  }
        
        
        
        
        {
            TestWork [] works = new TestWork[]{
                    new TestWork("w1", null, Constraint.ASAP, null, null, "07.05.10", "07.20.10", null),
                    TestWork.createFDWork("w2", "w1", Constraint.FD,  "07.01.10", "07.09.10", null, "07.05.10", "07.20.10", null),
                    TestWork.createFDWork("w3", "w1", Constraint.FD,  "07.01.10", "07.09.10", null, "07.07.10", "07.09.10", null),
            };
            
            works[1].setStatus(WorkStatus.COMPLETED);
            works[1].setActualStart("07.05.10");
            works[1].setActualEnd("07.20.10");
            
            works[2].setStatus(WorkStatus.OFF_TRACK);
            works[2].setActualStart("07.07.10");
            utils.run(works);
        }
        
        {
            final TestWork [] works = new TestWork[]{
                    new TestWork("w1", null, Constraint.ASAP, null, null, "07.05.10", "07.20.10", null),
                    TestWork.createFDWork("w2", "w1", Constraint.FD,  "07.01.10", "07.09.10", null, "07.12.10", "07.12.10", null),
                    TestWork.createFDWork("w3", "w1", Constraint.FD,  "07.01.10", "07.09.10", null, "07.05.10", "07.20.10", null),
            };
            
            works[1].setStatus(WorkStatus.OFF_TRACK);
            works[1].setActualStart("07.12.10");
            
            works[2].setStatus(WorkStatus.COMPLETED);
            works[2].setActualStart("07.05.10");
            works[2].setActualEnd("07.20.10");

            new ThrowExceptionTest("w2", FDConstraintProblem.class) {
                void invoke() {
                    utils.run(works);
                }
            };
            
            utils.testWorks(works);
            
        }
    }
   
    public void test64017() {
        TestWork [] works = new TestWork[]{
                new TestWork("w1", null, Constraint.ASAP, null, null, "06.19.09", "09.14.09", null),
                new TestWork("w2","w1", Constraint.ASAP, null, 20, "06.19.09", "09.14.09", null),
                new TestWork("w3","w2", Constraint.ASAP, null, 20, "06.19.09", "07.16.09", null),
                new TestWork("w4","w2", Constraint.SNET, null, 20, "08.18.09", "09.14.09", "w3"),
        };
        
        works[0].setActualStart("06.19.09");
        works[0].setLatestDates("07.21.09", "09.14.09", false);
        works[1].setLatestDates("07.21.09", "09.14.09", false);
        works[2].setLatestDates("07.21.09", "08.17.09", false);
        works[3].setLatestDates("08.18.09", "09.14.09", true);
        utils.run(works);
    }
    
    public void testBigHierarchy2() {
        TestWork works[] = {
                new TestWork("jwh 01oct2009", null, Constraint.ASAP, null, 10, "09.09.09", "09.09.09",                           null       ),
                new TestWork("Scope", "jwh 01oct2009", Constraint.ASAP, null, 10, "09.09.09", "09.09.09",                            null       ),
                new TestWork("Determinproject scope", "Scope", Constraint.ASAP, null, 10, "09.09.09", "09.09.09",                        null       ),
                new TestWork("Securproject sponsorship", "Scope", Constraint.ASAP, null, 10, "09.09.09", "09.09.09",                         3          ),
                new TestWork("Definpreliminary resources", "Scope", Constraint.ASAP, null, 10, "09.09.09", "09.09.09",                   4          ),
                new TestWork("Securcorresources", "Scope", Constraint.ASAP, null, 10, "09.09.09", "09.09.09",                        5          ),
                new TestWork("Scopcomplete", "Scope", Constraint.ASAP, null, 0, "09.09.09", "09.09.09",                             6          ),
                new TestWork("Analysis/SoftwarRequirements", "jwh 01oct2009", Constraint.ASAP, null, 10, "09.09.09", "09.09.09",                 null       ),
                new TestWork("Conduct needs analysis", "Analysis/SoftwarRequirements", Constraint.ASAP, null, 10, "09.09.09", "09.09.09",            7          ),
                new TestWork("Draft preliminary softwarspecific...", "Analysis/SoftwarRequirements", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",  9          ),
                new TestWork("Develop preliminary budget", "Analysis/SoftwarRequirements", Constraint.ASAP, null, 10, "09.09.09", "09.09.09",        10         ),
                new TestWork("Review softwarspecifications/budg...", "Analysis/SoftwarRequirements", Constraint.ASAP, null, 10, "09.09.09", "09.09.09",  11         ),
                new TestWork("Incorporatfeedback on softwarsp...", "Analysis/SoftwarRequirements", Constraint.ASAP, null, 10, "09.09.09", "09.09.09",    12         ),
                new TestWork("Develop delivery timeline", "Analysis/SoftwarRequirements", Constraint.ASAP, null, 10, "09.09.09", "09.09.09",             13         ),
                new TestWork("Obtain approvals to proceed (concep...", "Analysis/SoftwarRequirements", Constraint.ASAP, null, 10, "09.09.09", "09.09.09",    14         ),
                new TestWork("Securrequired resources", "Analysis/SoftwarRequirements", Constraint.ASAP, null, 100, "09.09.09", "09.09.09",           15         ),
                new TestWork("Analysis complete", "Analysis/SoftwarRequirements", Constraint.ASAP, null, 0, "09.09.09", "09.09.09",                 16         ),
                new TestWork("Design", "jwh 01oct2009", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                           null       ),
                new TestWork("Review preliminary softwarspecifi...", "Design", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                17         ),
                new TestWork("Develop functional specifications", "Design", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",               19         ),
                new TestWork("one", "Develop functional specifications", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                                          null       ),
                new TestWork("two", "Develop functional specifications", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                                          21         ),
                new TestWork("three", "Develop functional specifications", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                                            null       ),
                new TestWork("Develop prototypbased on function...", "Design", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                20         ),
                new TestWork("Project Templatshort floating task", "Design", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",              19         ),
                new TestWork("Review functional specifications", "Design", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                24         ),
                new TestWork("Incorporatfeedback into functiona...", "Design", Constraint.ASAP, null, 10, "09.09.09", "09.09.09",                26),//, 25     ),
                new TestWork("Obtain approval to proceed", "Design", Constraint.ASAP, null, 10, "09.09.09", "09.09.09",                  27         ),
                new TestWork("Design complete", "Design", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                             28         ),
                new TestWork("Development", "jwh 01oct2009", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                      null       ),
                new TestWork("Review functional specifications", "Development", Constraint.ASAP, null, 10, "09.09.09", "09.09.09",               29         ),
                new TestWork("Identify modular/tiered design para...", "Development", Constraint.ASAP, null, 100, "09.09.09", "09.09.09",             31         ),
                new TestWork("Assign development staff", "Development", Constraint.ASAP, null, 010, "09.09.09", "09.09.09",                   32         ),
                new TestWork("Develop code", "Development", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                       33         ),
                new TestWork("Developer testing (primary deb","Development", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",             34         ),
                new TestWork("Development complete", "Development", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                   35         ),
                new TestWork("Testing", "jwh 01oct2009", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                          null       ),
                new TestWork("Develop unit test plans using produ...", "Testing", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                 29         ),
                new TestWork("Develop integration test plans usin...", "Testing", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                 29         ),
                new TestWork("Unit Testing", "Testing", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                           null       ),
                new TestWork("Review modular code", "Unit Testing", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                   36),// 38     ),
                new TestWork("Test component modules to product s...", "Unit Testing", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",            36), //41     ),
                new TestWork("Identify anomalies to product speci...", "Unit Testing", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",            42         ),
                new TestWork("Modify code", "Unit Testing", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                       43         ),
                new TestWork("Re-test modified code", "Unit Testing", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                     44         ),
                new TestWork("Unit testing complete", "Unit Testing", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                     45         ),
                new TestWork("Integration Testing", "Testing", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                        null       ),
                new TestWork("Test modulintegration", "Integration Testing", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",              46         ),
                new TestWork("Identify anomalies to specification...", "Integration Testing", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",         48         ),
                new TestWork("Modify code", "Integration Testing", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                    49         ),
                new TestWork("Re-test modified code", "Integration Testing", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",              50         ),
                new TestWork("Integration testing complete", "Integration Testing", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",           51         ),
                new TestWork("Training", "jwh 01oct2009", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                             null       ),
                new TestWork("Develop training specifications for...", "Training", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",            29         ),
                new TestWork("Develop training specifications for...", "Training", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",            29         ),
                new TestWork("Identify training delivery methodol...", "Training", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",            29         ),
                new TestWork("Develop training materials", "Training", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                    36),// 54     ),
                new TestWork("Conduct training usability study", "Training", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",              57         ),
                new TestWork("Finaliztraining materials", "Training", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                     58         ),
                new TestWork("Develop training delivery mechanism...", "Training", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",            59         ),
                new TestWork("Training materials complete", "Training", Constraint.ASAP, null, 0, "09.09.09", "09.09.09",                   60         ),
                new TestWork("Documentation", "jwh 01oct2009", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                        null       ),
                new TestWork("Develop Help specification", "Documentation", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",               29         ),
                new TestWork("Develop Help system", "Documentation", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                  34), //63     ),
                new TestWork("Review Help documentation", "Documentation", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                64         ),
                new TestWork("IncorporatHelp documentation feed...", "Documentation", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",             65         ),
                new TestWork("Develop user manuals specifications...", "Documentation", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",           29         ),
                new TestWork("Develop user manuals", "Documentation", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                     34), //67     ),
                new TestWork("Review all user documentation", "Documentation", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                68         ),
                new TestWork("Incorporatuser documentation feed...", "Documentation", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",             69         ),
                new TestWork("Documentation complete", "Documentation", Constraint.ASAP, null, 0, "09.09.09", "09.09.09",                   66), //70     ),
                new TestWork("Pilot", "jwh 01oct2009", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                            null       ),
                new TestWork("Identify test group", "Pilot", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                      17         ),
                new TestWork("Develop softwardelivery mechanism...", "Pilot", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                 73         ),
                new TestWork("Install/deploy software", "Pilot", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                      52), //61     ),
                new TestWork("Obtain user feedback", "Pilot", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                         75         ),
                new TestWork("Evaluattesting information", "Pilot", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                   76         ),
                new TestWork("Pilot complete", "Pilot", Constraint.ASAP, null, 0, "09.09.09", "09.09.09",                           77         ),
                new TestWork("Deployment", "jwh 01oct2009", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                       null       ),
                new TestWork("Determinfinal deployment strategy...", "Deployment", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                                         78         ),
                new TestWork("Develop deployment methodology", "Deployment",  Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                                               80         ),
                new TestWork("Securdeployment resources", "Deployment", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                                           81         ),
                new TestWork("Train support staff", "Deployment", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                                                   82         ),
                new TestWork("Deploy software", "Deployment",     Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                                               83         ),
                new TestWork("Deployment complete", "Deployment", Constraint.ASAP, null, 0, "09.09.09", "09.09.09",                                                   84         ),
                new TestWork("Post Implementation Review", "jwh 01oct2009", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",               null       ),
                new TestWork("Document lessons learned", "Post Implementation Review", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",                          85         ),
                new TestWork("Distributto team members", "Post Implementation Review", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",          87         ),
                new TestWork("Creatsoftwarmaintenancteam", "Post Implementation Review", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",            88         ),
                new TestWork("Post implementation review complete...", "Post Implementation Review", Constraint.ASAP, null, 0, "09.09.09", "09.09.09",    89         ),
                new TestWork("Softwardevelopment templatcompl...","jwh 01oct2009", Constraint.ASAP, null, 1, "09.09.09", "09.09.09",            90         ),
      
        };
        
        utils.run(works, false);
    }
   
    public void testBigHierarchy() {
        
        // note:
        // if elements count 10000, 6 deps per bean  and total duration about 3 year then
        // calculatioin time expected to be about 2-3 seconds
        System.out.println("Start");
        int n = 10000;
        TestWorkBean beans[] = new TestWorkBean[n];
        boolean using[] = new boolean[n];
        List<TestWorkBean> children[] = new List[n];
        for (int i = 0; i < n; ++i) {
            TestWorkBean w1 = new TestWorkBean("w" + i, "id" + i);
            w1.setPlannedStartDate(WorkScheduleTestUtils.formatDate("01.01.08"));
            w1.setPlannedLaborTime(Duration.getDaysDuration(50.f));//1.f + (int)(Math.random() * 100)));
            w1.setOwner(user);
            beans[i] = w1;
            using[i] = false;
            children[i] = new ArrayList<TestWorkBean>();
            w1.setConstraintType(Constraint.ASAP);
        }

        BeansTree tree = new BeansTree(beans[0]);
        for (int i = 1; i < n; ++i) {
            tree.addChild(beans[0], beans[i]);
        }
        
        for (int i = 1; i < n - 20; ++i) {
//            tree.addDependency(beans[i + 4], beans[1 + i % 20], DependencyType.FS_DEPENDENCY, 0);
//            tree.addDependency(beans[i + 7], beans[1 + i % 20], DependencyType.FS_DEPENDENCY, 2);
//            tree.addDependency(beans[i + 2], beans[1 + i % 20], DependencyType.FS_DEPENDENCY, 3);
//            tree.addDependency(beans[i + 8], beans[1 + i % 20], DependencyType.SF_DEPENDENCY, 0);
//            tree.addDependency(beans[i + 3], beans[1 + i % 20], DependencyType.SS_DEPENDENCY, 8);
//            tree.addDependency(beans[i + 5], beans[1 + i % 20], DependencyType.FF_DEPENDENCY, -30);
            
            tree.addDependency(new Dependency(beans[1 + i % 20], beans[i + 4], DependencyType.FS_DEPENDENCY, 0));
            tree.addDependency(new Dependency(beans[1 + i % 20], beans[i + 7], DependencyType.FS_DEPENDENCY, 2));
            tree.addDependency(new Dependency(beans[1 + i % 20], beans[i + 2], DependencyType.FS_DEPENDENCY, 3));
            tree.addDependency(new Dependency(beans[1 + i % 20], beans[i + 8], DependencyType.SF_DEPENDENCY, 0));
            tree.addDependency(new Dependency(beans[1 + i % 20], beans[i + 3], DependencyType.SS_DEPENDENCY, 8));
            tree.addDependency(new Dependency(beans[1 + i % 20], beans[i + 5], DependencyType.FF_DEPENDENCY, -30));
        }
   
        System.out.println("Start2");
        long t = System.currentTimeMillis();
        WorkSchedule ws = new WorkSchedule(new ScheduleData(tree, user, WBSTestHelper.getVisit()));
        ws.run();
        System.out.println(System.currentTimeMillis() - t);
        
        Iterator it = ws.getChildren(beans[0]);
        System.out.println(beans[0].getSystemLaborTime());
        while(it.hasNext()) {
            TestWorkBean b = (TestWorkBean)it.next();
           // System.out.println(b.getName() + " " + b.getSystemStartDate() + " " + b.getSystemEndDate());
        }
    }
    
    public void testBigHierarchy1() {
        
        // test deep hierarchy
        // note:
        // if elements count 10000, 6 deps per bean  and total duration about 3 year then
        // calculatioin time expected to be about 2-3 seconds
        System.out.println("Start");
        int n = 1000;
        int worksPerLevel = 2;
        TestWorkBean beans[] = new TestWorkBean[n];
        boolean using[] = new boolean[n];
        List<TestWorkBean> children[] = new List[n];
        
        
        final List<CreateWorkBean> works = new ArrayList<CreateWorkBean>();
        WorkTypeBean type =  WBSTestHelper.getWorkTypeBean(Work.TYPE, null);
        for (int i = 0; i < n; ++i) {
            TestWorkBean w1 = new TestWorkBean("w" + i, "id" + i);
            w1.setPlannedStartDate(WorkScheduleTestUtils.formatDate("01.01.08"));
            w1.setPlannedLaborTime(Duration.getDaysDuration(5.f));//1.f + (int)(Math.random() * 100)));
            w1.setOwner(user);
            beans[i] = w1;
            using[i] = false;
            children[i] = new ArrayList<TestWorkBean>();
            w1.setConstraintType(Constraint.ASAP);
            works.add(new CreateWorkBean(type, w1.getName()));
        }

        beans[500].setConstraintType(Constraint.SNET);
        beans[500].setPlannedStartDate(utils.formatDate("01.01.20"));
        BeansTree tree = new BeansTree(beans[0]);
        for (int i = 1; i < n; ++i) {
            int parent = (i-1)/worksPerLevel;
            tree.addChild(beans[parent], beans[i]);
            works.get(i).setParent(parent);
        }
        
        Collection dependencies[] = new Collection[n];
        for(int i = 0; i < n; ++i) {
            dependencies[i] = new HashSet<IndexedDependency>();
        }
        int prev = worksPerLevel; // = w^k;
        for (int l = 1; l < 32; ++l) {
            int start = (prev - 1)/(worksPerLevel - 1);
            prev *= worksPerLevel;
            int end = (prev - 1)/(worksPerLevel - 1); 
            if (start >= n) {
                break;
            }
            
            int largest = Math.min(end, n) - 1; 
            for (int j = start; j < largest;++j) {
                tree.addDependency(new Dependency(beans[j], beans[j + 1], DependencyType.FS_DEPENDENCY, 0));
                dependencies[j].add(new IndexedDependency(-(j+2), DependencyType.FS_DEPENDENCY, 0));
            }
        }
        
        for (int i = 1; i < n; ++i) {
            works.get(i).setDependencies(dependencies[i]);
        }
        //WBSTestUtils.printTree(tree);
        
       // ProjectEditor pe = new ProjectEditor(utils.getVisit());
       // pe.loadTree(WBSTestUtils.getWork());
       // System.out.println(pe.getTree().getRoot().getId());
        //pe.addWorks((WorkBean)pe.getTree().getRoot(), 0, works);
        
//        Iterator it1 = pe.getTree().getChildrenIterator(pe.getTree().getRoot());
//        while(it1.hasNext()) {
//            System.out.println(((WBSBean)it1.next()).getId());
//        }
       // pe.saveTree();
   
        
        System.out.println("Start2");
        long t = System.currentTimeMillis();
        WorkSchedule ws = new WorkSchedule(new ScheduleData(tree, user, WBSTestHelper.getVisit()));
        System.out.println("Start3");
        ws.run();
        System.out.println(System.currentTimeMillis() - t);
        
        Iterator it = ws.getChildren(beans[0]);
        System.out.println(beans[0].getSystemLaborTime());
        while(it.hasNext()) {
            TestWorkBean b = (TestWorkBean)it.next();
           // System.out.println(b.getName() + " " + b.getSystemStartDate() + " " + b.getSystemEndDate());
        }
    }
    
    public void testLatestDates() {
        {
            TestWork works[] = new TestWork[]{
                new TestWork("w1", null, Constraint.ASAP, null,  1, "09.21.09", "09.22.09",null),
                new TestWork("w2", "w1", Constraint.ASAP, null,  1, "09.21.09", "09.21.09",null),
                new TestWork("w3", "w2", Constraint.ASAP, null,  1, "09.21.09", "09.21.09",null),
                new TestWork("w4", "w3", Constraint.ASAP, null,  1, "09.21.09", "09.21.09",null),
                new TestWork("w5", "w4", Constraint.ASAP, null,  1, "09.21.09", "09.21.09",null),
                new TestWork("w6", "w5", Constraint.ASAP, null,  1, "09.21.09", "09.21.09",null),
                new TestWork("w7", "w6", Constraint.ASAP, null,  1, "09.21.09", "09.21.09",null),
                new TestWork("w8", "w7", Constraint.ASAP, null,  1, "09.21.09", "09.21.09",null),
                new TestWork("w9", "w8", Constraint.ASAP, null,  1, "09.21.09", "09.21.09",null),
                new TestWork("w10", "w1", Constraint.ASAP, null,  1, "09.22.09", "09.22.09","w2"),
            };
            works[0].setLatestDates("09.21.09", "09.22.09", true);
            works[1].setLatestDates("09.21.09", "09.21.09", true);
            works[2].setLatestDates("09.21.09", "09.21.09", true);
            works[3].setLatestDates("09.21.09", "09.21.09", true);
            works[4].setLatestDates("09.21.09", "09.21.09", true);
            works[5].setLatestDates("09.21.09", "09.21.09", true);
            works[6].setLatestDates("09.21.09", "09.21.09", true);
            works[7].setLatestDates("09.21.09", "09.21.09", true);
            works[8].setLatestDates("09.21.09", "09.21.09", true);
            works[9].setLatestDates("09.22.09", "09.22.09", true);
            utils.run(works);
            
        }
        
        {
            TestWork works[] = new TestWork[]{
                new TestWork("w1", null, Constraint.ASAP, null,  1, "09.21.09", "09.22.09",null),
                new TestWork("w2", "w1", Constraint.ASAP, null,  1, "09.21.09", "09.21.09",null),
                new TestWork("w3", "w2", Constraint.ASAP, null,  1, "09.21.09", "09.21.09",null),
                new TestWork("w4", "w3", Constraint.ASAP, null,  1, "09.21.09", "09.21.09",null),
                new TestWork("w5", "w1", Constraint.ASAP, null,  1, "09.22.09", "09.22.09","w2"),
            };
            
            works[0].setLatestDates("09.21.09", "09.22.09", true);
            works[1].setLatestDates("09.21.09", "09.21.09", true);
            works[2].setLatestDates("09.21.09", "09.21.09", true);
            works[3].setLatestDates("09.21.09", "09.21.09", true);
            works[4].setLatestDates("09.22.09", "09.22.09", true);
            utils.run(works);
        }
        
        {
            TestWork works[] = new TestWork[]{
                new TestWork("w1", null, Constraint.ASAP, null,   5, "09.01.09", "09.14.09",null),
                new TestWork("w2", "w1", Constraint.ASAP, null,   5, "09.01.09", "09.07.09",null),
                new TestWork("w3", "w1", Constraint.ASAP, null,   5, "09.08.09", "09.14.09", "w2", "FS"),
                new TestWork("w4", "w1", Constraint.ASAP, null,   7, "09.01.09", "09.09.09",null),
            };
            
            works[0].setLatestDates("09.01.09", "09.14.09", true);
            works[1].setLatestDates("09.01.09", "09.07.09", true);
            works[2].setLatestDates("09.08.09", "09.14.09", true);
            works[3].setLatestDates("09.04.09", "09.14.09", false);
            utils.run(works);
        }
        
        {
            TestWork works[] = new TestWork[]{
                new TestWork("w1", null, Constraint.ASAP, null,   5, "09.01.09", "09.22.09",null),
                new TestWork("w2", "w1", Constraint.ASAP, null,   5, "09.01.09", "09.07.09",null),
                new TestWork("w3", "w1", Constraint.ASAP, null,   5, "09.08.09", "09.14.09", "w2", "FS"),
                new TestWork("w4", "w1", Constraint.ASAP, null,   16, "09.01.09", "09.22.09",null),
            };
            
            works[0].setLatestDates("09.01.09", "09.22.09", true);
            works[1].setLatestDates("09.09.09", "09.15.09", false);
            works[2].setLatestDates("09.16.09", "09.22.09", false);
            works[3].setLatestDates("09.01.09", "09.22.09", true);
            utils.run(works);
        }
        
        
        {
            // ALAP
            TestWork works[] = new TestWork[]{
                new TestWork("w1", null, Constraint.ASAP, null,   5, "09.01.09", "09.22.09",null),
                new TestWork("w2", "w1", Constraint.ASAP, null,   5, "09.01.09", "09.07.09",null),
                new TestWork("w3", "w1", Constraint.ALAP, null,   5, "09.16.09", "09.22.09", "w2", "FS"),
                new TestWork("w4", "w1", Constraint.ASAP, null,   16, "09.01.09", "09.22.09",null),
            };
            
            works[0].setLatestDates("09.01.09", "09.22.09", true);
            works[1].setLatestDates("09.09.09", "09.15.09", false);
            works[2].setLatestDates("09.16.09", "09.22.09", true);
            works[3].setLatestDates("09.01.09", "09.22.09", true);
            utils.run(works);
        }
        
        {
            //SNET
            TestWork works[] = new TestWork[]{
                new TestWork("w1", null, Constraint.ASAP, null,         5, "09.01.09", "09.22.09",null),
                new TestWork("w2", "w1", Constraint.SNET, "09.03.09",   5, "09.03.09", "09.09.09",null),
                new TestWork("w3", "w1", Constraint.FNLT, "09.20.09",   5, "09.10.09", "09.16.09", "w2", "FS"),
                new TestWork("w4", "w1", Constraint.ASAP, null,   16, "09.01.09", "09.22.09",null),
            };
            
            works[0].setLatestDates("09.01.09", "09.22.09", true);
            works[1].setLatestDates("09.07.09", "09.11.09", false);
            works[2].setLatestDates("09.14.09", "09.18.09", false);
            works[3].setLatestDates("09.01.09", "09.22.09", true);
            utils.run(works);
        }
        
        {
            // SNET again.
            TestWork works[] = new TestWork[]{
                new TestWork("w1", null, Constraint.ASAP, null,         5, "09.03.09", "09.16.09",null),
                new TestWork("w2", "w1", Constraint.SNET, "09.03.09",   5, "09.03.09", "09.09.09",null),
                new TestWork("w3", "w1", Constraint.FNLT, "09.20.09",   5, "09.10.09", "09.16.09", "w2", "FS"),
            };
            
            works[0].setLatestDates("09.03.09", "09.16.09", true);
            works[1].setLatestDates("09.03.09", "09.09.09", true);
            works[2].setLatestDates("09.10.09", "09.16.09", true);
            utils.run(works);
        }
        
        {
          //SNET
          TestWork works[] = new TestWork[]{
              new TestWork("w1", null, Constraint.ASAP, null,   5, "09.01.09", "09.29.09",null),
              new TestWork("w2", "w1", Constraint.ASAP, null,   5, "09.23.09", "09.29.09", "w3"),
              new TestWork("w3", "w1", Constraint.ASAP, null,   5, "09.01.09", "09.22.09", null),
              new TestWork("w4", "w3", Constraint.ASAP, null,   16, "09.01.09", "09.22.09",null),
          };
          
          works[0].setLatestDates("09.01.09", "09.29.09", true);
          works[1].setLatestDates("09.23.09", "09.29.09", true);
          works[2].setLatestDates("09.01.09", "09.22.09", true);
          works[3].setLatestDates("09.01.09", "09.22.09", true);
          utils.run(works);
        }
        {
            //Complex
            TestWork works[] = new TestWork[]{
                new TestWork("w1", null, Constraint.ASAP, null,   10,"09.01.09", "09.23.09",null),
                new TestWork("w2", "w1", Constraint.ASAP, null,   5, "09.01.09", "09.07.09",null),
                new TestWork("w3", "w1", Constraint.ASAP, null,   5, "09.15.09", "09.21.09","w2;FS2;w5;FS2"),
                new TestWork("w4", "w1", Constraint.ASAP, null,   3, "09.21.09", "09.23.09","w3;FS-1;w6;FS-2"),
                new TestWork("w5", "w1", Constraint.ASAP, null,   8, "09.01.09", "09.10.09",null),
                new TestWork("w6", "w1", Constraint.ASAP, null,   9, "09.09.09", "09.17.09",null),
                new TestWork("w7", "w6", Constraint.ASAP, null,   7, "09.09.09", "09.17.09","w2;FS1"),
            };
            
            works[0].setLatestDates("09.01.09", "09.23.09", true);
            works[1].setLatestDates("09.04.09", "09.10.09", false);
            works[2].setLatestDates("09.15.09", "09.21.09", true);
            works[3].setLatestDates("09.21.09", "09.23.09", true);
            works[4].setLatestDates("09.01.09", "09.10.09", true);
            works[5].setLatestDates("09.14.09", "09.22.09", false);
            works[6].setLatestDates("09.14.09", "09.22.09", false);
            utils.run(works);
        }
        
        {
            TestWork [] works = new TestWork[]{
                new TestWork("w1", null, Constraint.SNET, null, null,  "08.19.09", "08.27.09", null),
                new TestWork("w2","w1", Constraint.ASAP, null, 5,      "08.19.09", "08.25.09", null),
                new TestWork("w3","w1", Constraint.ASAP, null, null,   "08.25.09", "08.25.09", "w2"),
                new TestWork("w4","w1", Constraint.ASAP, null, null,   "08.25.09", "08.25.09", "w3"),
                new TestWork("w5","w1", Constraint.ASAP, null, 2,   "08.26.09", "08.27.09", "w4"),
            };
            
            works[0].setLatestDates("08.19.09", "08.27.09", true);
            works[1].setLatestDates("08.19.09", "08.25.09", true);
            works[2].setLatestDates("08.25.09", "08.25.09", true);
            works[3].setLatestDates("08.25.09", "08.25.09", true);
            works[4].setLatestDates("08.26.09", "08.27.09", true);
            utils.run(works);
        }
        
        // FIX it
        {
            TestWork [] works = new TestWork[]{
                new TestWork("w1", null, Constraint.SNET, null, null,  "08.19.09", "09.18.09", null),
                new TestWork("w2","w1", Constraint.ASAP, null, 5,      "08.19.09", "08.25.09", null),
                new TestWork("w3","w1", Constraint.ASAP, null, null,   "08.25.09", "08.25.09", "w2"),
                new TestWork("w4","w1", Constraint.ASAP, null, 15,     "08.26.09", "09.15.09", "w3"),
                new TestWork("w5","w1", Constraint.ASAP, null, null,   "09.15.09", "09.15.09", "w4"),
                new TestWork("w6","w1", Constraint.ASAP, null, 3,      "09.16.09", "09.18.09", "w5"),
            };
            
            works[0].setLatestDates("08.19.09", "09.18.09", true);
            works[1].setLatestDates("08.19.09", "08.25.09", true);
            works[2].setLatestDates("08.25.09", "08.25.09", true);
            works[3].setLatestDates("08.26.09", "09.15.09", true);
            works[4].setLatestDates("09.15.09", "09.15.09", true);
            works[5].setLatestDates("09.16.09", "09.18.09", true);
            utils.run(works);
        }
        
        {
            TestWork [] works = new TestWork[]{
                new TestWork("w1", null, Constraint.SNET, null, null,  "08.19.09", "09.22.09", null),
                new TestWork("w2","w1", Constraint.ASAP, null, 5,      "08.19.09", "08.25.09", null),
                new TestWork("w3","w1", Constraint.ASAP, null, 1,      "08.26.09", "08.26.09", "w2"),
                new TestWork("w4","w1", Constraint.ASAP, null, 15,     "08.27.09", "09.16.09", "w3"),
                new TestWork("w5","w1", Constraint.ASAP, null, 1,      "09.17.09", "09.17.09", "w4"),
                new TestWork("w6","w1", Constraint.ASAP, null, 3,      "09.18.09", "09.22.09", "w5"),
            };
            
            works[0].setLatestDates("08.19.09", "09.22.09", true);
            works[1].setLatestDates("08.19.09", "08.25.09", true);
            works[2].setLatestDates("08.26.09", "08.26.09", true);
            works[3].setLatestDates("08.27.09", "09.16.09", true);
            works[4].setLatestDates("09.17.09", "09.17.09", true);
            works[5].setLatestDates("09.18.09", "09.22.09", true);
            utils.run(works);
        }
    }
    
    public void testLags() {
        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.22.08",null),
            new TestWork("w2", "w1", Constraint.ASAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ASAP, null,   9, "01.10.08", "01.22.08", "w2", "FS"),
        });
        
        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.23.08",null),
            new TestWork("w2", "w1", Constraint.ASAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ASAP, null,   9, "01.11.08", "01.23.08", "w2", "FS1"),
        });
        
        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.25.08",null),
            new TestWork("w2", "w1", Constraint.ASAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ASAP, null,   9, "01.15.08", "01.25.08", "w2", "FS3"),
        });

        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.21.08",null),
            new TestWork("w2", "w1", Constraint.ASAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ASAP, null,   9, "01.09.08", "01.21.08", "w2", "FS-1"),
        });

        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.18.08",null),
            new TestWork("w2", "w1", Constraint.ASAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ASAP, null,   9, "01.08.08", "01.18.08", "w2", "FS-2"),
        });

        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.11.08",null),
            new TestWork("w2", "w1", Constraint.ASAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ASAP, null,   9, "01.01.08", "01.11.08", "w2", "SS"),
        });
        
        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.15.08",null),
            new TestWork("w2", "w1", Constraint.ASAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ASAP, null,   9, "01.03.08", "01.15.08", "w2", "SS2"),
        });
        
        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.11.08",null),
            new TestWork("w2", "w1", Constraint.ASAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ASAP, null,   9, "01.01.08", "01.11.08", "w2", "SS-2"),
        });
        
        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.11.08",null),
            new TestWork("w2", "w1", Constraint.ASAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ASAP, null,   9, "01.01.08", "01.11.08", "w2", "FF"),
        });
        
        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.11.08",null),
            new TestWork("w2", "w1", Constraint.ASAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ASAP, null,   9, "01.01.08", "01.11.08", "w2", "FF1"),
        });
       
        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.14.08",null),
            new TestWork("w2", "w1", Constraint.ASAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ASAP, null,   9, "01.02.08", "01.14.08", "w2", "FF3"),
        });
        
        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.09.08",null),
            new TestWork("w2", "w1", Constraint.ASAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ASAP, null,   3, "01.03.08", "01.07.08", "w2", "FF-2"),
        });

    }
    
    public void testCalendarsSimple() {
        DateCalculator dc1 = new DateCalculatorImpl(getCalendar(8.f, 8.f, 8.f), TimeZone.getDefault());
        DateCalculator dc2 = new DateCalculatorImpl(getCalendar(8.f), TimeZone.getDefault());
        
        
        // TODO 
        // FIX TestWorkBean - WorkSchedule does not used calculator form bean
        {
            TestWorkBean w1 = new TestWorkBean("w1", "id1");
            w1.setPlannedStartDate(WorkScheduleTestUtils.formatDate("01.01.08"));
            w1.setPlannedLaborTime(Duration.getDaysDuration(0.f));
            
            TestWorkBean w2 = new TestWorkBean("w2", "id2");
            w2.setPlannedStartDate(WorkScheduleTestUtils.formatDate("01.01.08"));
            w2.setPlannedLaborTime(Duration.getDaysDuration(5.f));
            
            TestWorkBean w3 = new TestWorkBean("w3", "id3");
            w3.setPlannedStartDate(WorkScheduleTestUtils.formatDate("02.01.08"));
            w3.setConstraintType(Constraint.SNET);
            w3.setPlannedLaborTime(Duration.getDaysDuration(10.f));
            
            BeansTree tree = new BeansTree(w1);
            tree.addChild(w1, w2);
            tree.addChild(w1, w3);
            tree.addDependency(new Dependency(w2, w3, DependencyType.FS_DEPENDENCY, 0));
            
            WorkSchedule ws = new WorkSchedule(new ScheduleData(tree, user, WBSTestHelper.getVisit()));
            ws.run();
            assertTrue(Util.isNullEmpty(ws.checkForProblems()));
            assertTrue(ws.getModifiedBeans().size() == 3);
            
            List<TestSchedule> result = new ArrayList<TestSchedule>();
            result.add(new TestSchedule(w1, "01.01.08", "04.07.08"));
            result.add(new TestSchedule(w2, "01.01.08", "01.09.08"));
            result.add(new TestSchedule(w3, "02.04.08", "04.07.08"));
            utils.testWorks(ws.getModifiedBeans(), result);
    
            ws.run();
            assertTrue(Util.isNullEmpty(ws.checkForProblems()));
            // dates are not changed - displays the same list
            assertTrue(ws.getModifiedBeans().size() == 3);
        }
    }
    
    public void testDifferentCalendars() {
        {
        	CalendarBean cb1 = getCalendar(8.f, 8.f, 8.f);
        	CalendarBean cb2 = getCalendar(8.f, 8.f, 8.f, 8.f, 8.f);
            DateCalculator dc1 = new DateCalculatorImpl(cb1, TimeZone.getDefault());
            DateCalculator dc2 = new DateCalculatorImpl(cb2, TimeZone.getDefault());
            
            TestWorkBean w1 = new TestWorkBean("w1", "id1");
            w1.setPlannedStartDate(WorkScheduleTestUtils.formatDate("01.01.08"));
            w1.setPlannedLaborTime(Duration.getDaysDuration(10.f));
            w1.setProjectControl(ProjectControl.CALENDAR, cb1);
            
            BeansTree tree = new BeansTree(w1);
            WorkSchedule ws = new WorkSchedule(new ScheduleData(tree, user, WBSTestHelper.getVisit()));
            ws.run();
            
            List<TestSchedule> result = new ArrayList<TestSchedule>();
            result.add(new TestSchedule(w1, "01.01.08", "01.22.08"));
            utils.testWorks(ws.getModifiedBeans(), result);
            
            w1.setProjectControl(ProjectControl.CALENDAR, cb2);
            
            ws.run();
            
            result = new ArrayList<TestSchedule>();
            result.add(new TestSchedule(w1, "01.01.08", "01.14.08"));
            utils.testWorks(ws.getModifiedBeans(), result);
//            
//            w1.getDateCalculators().clear();
//            w1.addDateCalculator(dc1);
//            w1.addDateCalculator(dc2);
//            
//            ws.run();
//            
//            result = new ArrayList<TestSchedule>();
//            result.add(new TestSchedule(w1, "01.01.08", "01.22.08"));
//            utils.testWorks(ws.getModifiedBeans(), result);
        }
        {
            // calendar jan of 2008
//               01 02 03 04 05 06
//            07 08 09 10 11 12 13
//            14 15 16 17 18 19 20
//            21 22 23 24 25 26 27
//            28 29 30 31
            TestWorkBean w1 = new TestWorkBean("w1", "id1");
            w1.setPlannedStartDate(WorkScheduleTestUtils.formatDate("01.01.08"));
            w1.setPlannedLaborTime(Duration.getDaysDuration(0.f));
            w1.setProjectControl(ProjectControl.CALENDAR, getCalendar(8.f, 8.f, 8.f, 8.f, 8.f));
            
            TestWorkBean w2 = new TestWorkBean("w2", "id2");
            w2.setPlannedStartDate(WorkScheduleTestUtils.formatDate("01.01.08"));
            w2.setPlannedLaborTime(Duration.getDaysDuration(5.f));
            w2.setProjectControl(ProjectControl.CALENDAR, getCalendar(8.f, 8.f, 8.f));
//            w2.addDateCalculator(new DateCalculatorImpl(getCalendar(8.f, 8.f, 8.f), TimeZone.getDefault()));
//            w2.addDateCalculator(new DateCalculatorImpl(getCalendar(0.f, 0.f, 0.f, 8.f), TimeZone.getDefault()));
            
            TestWorkBean w3 = new TestWorkBean("w3", "id3");
            w3.setPlannedStartDate(WorkScheduleTestUtils.formatDate("02.01.08"));
            w3.setPlannedLaborTime(Duration.getDaysDuration(10.f));
            w3.setProjectControl(ProjectControl.CALENDAR, getCalendar(0.f, 0.f, 0.f, 8.f, 8.f, 8.f));
//            w3.addDateCalculator(new DateCalculatorImpl(getCalendar(0.f, 0.f, 0.f, 8.f, 8.f, 8.f), TimeZone.getDefault()));
//            w3.addDateCalculator(new DateCalculatorImpl(getCalendar(8.f, 8.f, 8.f), TimeZone.getDefault()));
//            w3.addDateCalculator(new DateCalculatorImpl(getCalendar(0.0f, 0.8f, 0.f, 8.f), TimeZone.getDefault()));
            
            BeansTree tree = new BeansTree(w1);
            tree.addChild(w1, w2);
            tree.addChild(w1, w3);
            tree.addDependency(new Dependency(w2, w3, DependencyType.FS_DEPENDENCY, 0));
            
            WorkSchedule ws = new WorkSchedule(new ScheduleData(tree, user, WBSTestHelper.getVisit()));
            ws.run();
            
            List<TestSchedule> result = new ArrayList<TestSchedule>();
            result.add(new TestSchedule(w1, "01.03.08", "03.06.08"));
            result.add(new TestSchedule(w2, "01.03.08", "01.31.08"));
            result.add(new TestSchedule(w3, "02.01.08", "03.06.08"));
            utils.testWorks(ws.getModifiedBeans(), result);

        }
    }
    
    public void test62129() {
        TestWork [] works = new TestWork[]{
            new TestWork("w1", null, Constraint.SNET, null, null,  "08.19.09", "09.18.09", null),
            new TestWork("w2","w1", Constraint.ASAP, null, 5,      "08.19.09", "08.25.09", null),
            new TestWork("w3","w1", Constraint.ASAP, null, null,   "08.25.09", "08.25.09", "w2"),
            new TestWork("w4","w1", Constraint.ASAP, null, 15,     "08.26.09", "09.15.09", "w3"),
            new TestWork("w5","w1", Constraint.ASAP, null, null,   "09.15.09", "09.15.09", "w4"),
            new TestWork("w6","w1", Constraint.ASAP, null, 3,      "09.16.09", "09.18.09", "w5"),
        };
        utils.run(works);
    }

    public void testTestWorkBeans() {
       
        {
            TestWorkBean w1 = new TestWorkBean("w1", "id1");
            w1.setPlannedStartDate(WorkScheduleTestUtils.formatDate("01.01.08"));
            w1.setPlannedLaborTime(Duration.getDaysDuration(0.f));
            
            TestWorkBean w2 = new TestWorkBean("w2", "id2");
            w2.setPlannedStartDate(WorkScheduleTestUtils.formatDate("01.01.08"));
            w2.setPlannedLaborTime(Duration.getDaysDuration(5.f));
            
            TestWorkBean w3 = new TestWorkBean("w3", "id3");
            w3.setPlannedStartDate(WorkScheduleTestUtils.formatDate("02.01.08"));
            w3.setConstraintType(Constraint.SNET);
            w3.setPlannedLaborTime(Duration.getDaysDuration(10.f));
            
            BeansTree tree = new BeansTree(w1);
            tree.addChild(w1, w2);
            tree.addChild(w1, w3);
            tree.addDependency(new Dependency(w2, w3, DependencyType.FS_DEPENDENCY, 0));
            
            WorkSchedule ws = new WorkSchedule(new ScheduleData(tree, user, WBSTestHelper.getVisit()));
            ws.run();
            assertTrue(Util.isNullEmpty(ws.checkForProblems()));
            assertEquals(ws.getModifiedBeans().size(), 3);
            
            List<TestSchedule> result = new ArrayList<TestSchedule>();
            result.add(new TestSchedule(w1, "01.01.08", "02.14.08"));
            result.add(new TestSchedule(w2, "01.01.08", "01.07.08"));
            result.add(new TestSchedule(w3, "02.01.08", "02.14.08"));
            utils.testWorks(ws.getModifiedBeans(), result);
            
            // dates are not changed
            ws.run();
            assertTrue(Util.isNullEmpty(ws.checkForProblems()));
            assertTrue(ws.getModifiedBeans().size() == 3); // changes were not applied so nothing is changed

        }
        
        {
            TestWorkBean w1 = new TestWorkBean("w1", "id1");
            w1.setPlannedStartDate(WorkScheduleTestUtils.formatDate("01.01.08"));
            w1.setPlannedLaborTime(Duration.getDaysDuration(0.f));
            
            TestWorkBean w2 = new TestWorkBean("w2", "id2");
            w2.setPlannedStartDate(WorkScheduleTestUtils.formatDate("01.01.08"));
            w2.setPlannedLaborTime(Duration.getDaysDuration(5.f));
            
            TestWorkBean w3 = new TestWorkBean("w3", "id3");
            w3.setPlannedStartDate(WorkScheduleTestUtils.formatDate("01.01.08"));
            w3.setPlannedLaborTime(Duration.getDaysDuration(10.f));
            
            BeansTree tree = new BeansTree(w1);
            tree.addChild(w1, w2);
            tree.addChild(w1, w3);
            tree.addDependency(new Dependency(w2, w3, DependencyType.FS_DEPENDENCY, 0));
            
            WorkSchedule ws = new WorkSchedule(new ScheduleData(tree, user, WBSTestHelper.getVisit()));
            ws.run();
            assertTrue(Util.isNullEmpty(ws.checkForProblems()));
            assertTrue(ws.getModifiedBeans().size() == 3);
            
            List<TestSchedule> result = new ArrayList<TestSchedule>();
            result.add(new TestSchedule(w1, "01.01.08", "01.21.08"));
            result.add(new TestSchedule(w2, "01.01.08", "01.07.08"));
            result.add(new TestSchedule(w3, "01.08.08", "01.21.08"));
            utils.testWorks(ws.getModifiedBeans(), result);
            
            // dates are not changed - modifiedWorks is not changed
            ws.run();
            assertTrue(Util.isNullEmpty(ws.checkForProblems()));
            assertTrue(ws.getModifiedBeans().size() == 3); // changes were not applied so nothing is changed
       }
    }
   

    public void test56389() {
        TestWork [] works = new TestWork[]{
            new TestWork("w1_", null, Constraint.ASAP, null, null, "08.25.08", "12.31.08", null),
            new TestWork("w2","w1_", Constraint.ASAP, null, 22, "08.25.08", "09.24.08", null),
            new TestWork("w3","w1_", Constraint.ASAP, null, null, "09.29.08", "10.10.08", "w2"),
            new TestWork("w4","w3", Constraint.ASAP, null, 5, "10.06.08", "10.10.08", null),
            new TestWork("w5","w1_", Constraint.ASAP, null, 14, "10.13.08", "10.30.08", "w3"),
            new TestWork("w6","w1_", Constraint.ASAP, null, 22, "10.31.08", "12.01.08", "w5"),
            new TestWork("w7","w1_", Constraint.ASAP, null, 22, "12.02.08", "12.31.08", "w6"),
        };
        
        works[1].setActualStart("08.25.08");
        works[1].setActualEnd("09.24.08");
        works[2].setActualStart("09.29.08");
        works[3].setActualStart("10.06.08");
        
        utils.run(works);
    }
    
    public void test55222() {
        {
            // manual flag and actual dates will not throw an error
            TestWork [] works = new TestWork[]{
                TestWork.createManual("w1", null, Constraint.FNLT, "01.01.08", "01.04.08", 2, "01.16.08","02.12.08", null),
                TestWork.createManual("w2", "w1", Constraint.SNET, "02.01.08", "02.05.08", 5, null, null, null),
                TestWork.createManual("w3", "w2", Constraint.SNET, "02.01.09", "02.05.09", 5, null, null, null),
            };
            works[2].setActualStart("01.01.06");
            works[2].setActualEnd("01.10.06");
            
            works[1].setActualStart("01.01.07");
            works[1].setActualEnd("01.10.07");
            
            works[0].setActualStart("01.01.08");
            works[0].setActualEnd("01.10.08");
            utils.run(works, false);
        }

        new ThrowExceptionTest("w1", FNLTConstraintProblem.class) {
            void invoke() {
                TestWork [] works = new TestWork[]{
                    new TestWork("w1", null, Constraint.FNLT, "01.01.08", 2, "01.16.08","02.12.08", null),
                    new TestWork("w2", "w1", Constraint.SNET, "02.01.08", 5, null, null, null),
                    new TestWork("w3", "w2", Constraint.SNET, "02.01.09", 5, null, null, null),
                };
                utils.run(works, false);
            }
        };
    }
    
    public void test54317() {
        TestWork[] projects = {
                new TestWork("Schedule Test Template", null,                                                Constraint.ASAP, null, null, "05.16.08", "07.03.08", null),  
                new TestWork("Evaluation", "Schedule Test Template",                                        Constraint.ASAP, null, null, "05.16.08", "07.03.08", null),
                new TestWork("FormTeam", "Evaluation",                                                      Constraint.ASAP, null, null, "05.16.08", "05.21.08", null),
                new TestWork("Identify Team Members", "FormTeam",                                           Constraint.ASAP, null, 3,    "05.16.08", "05.20.08", null),
                new TestWork("Conduct review and kickoff", "FormTeam",                                      Constraint.ASAP, null, 1,    "05.21.08", "05.21.08", 4),
                new TestWork("Define Initial Scope & Valuatio...", "Evaluation",                            Constraint.ASAP, null, null, "05.22.08", "07.03.08", 3),
                new TestWork("Refine proposed transaction scope", "Define Initial Scope & Valuatio...",     Constraint.ASAP, null, null, "05.22.08", "06.25.08", null),
                new TestWork("Develop a preliminary Component Bus...", "Refine proposed transaction scope", Constraint.ASAP, null, 5,    "05.22.08", "05.28.08", null),
                new TestWork("Articulate what is in & out of ...", "Refine proposed transaction scope",     Constraint.ASAP, null, 5,    "05.29.08", "06.04.08", 8),
                new TestWork("Define Intellectual Property and R&...", "Refine proposed transaction scope", Constraint.ASAP, null, 10,   "06.05.08", "06.18.08", 9),
                new TestWork("Preliminary assessment of services ...", "Refine proposed transaction scope", Constraint.ASAP, null, null, "06.19.08", "06.25.08", 10),
                new TestWork("Develop likely TSA scenarios and mo...", "Preliminary assessment of services ...",Constraint.ASAP, null, 5,"06.19.08", "06.25.08", null),
                new TestWork("Develop carve out straw-man", "Preliminary assessment of services ...",       Constraint.ASAP, null, 5,    "06.19.08", "06.25.08", null),
                new TestWork("Scope and Valuation Criteria define...", "Refine proposed transaction scope", Constraint.ASAP, null,  null,"06.25.08", "06.25.08", 12),
                new TestWork("Perform Valuation Analysis", "Define Initial Scope & Valuatio...",            Constraint.ASAP, null, null, "06.26.08", "07.03.08", null),
                new TestWork("Perform Financial Valuation Analysi...", "Perform Valuation Analysis",        Constraint.ASAP, null, 6,    "06.26.08", "07.03.08", 7)
        };
        utils.run(projects);
    }
    
    public void testBug53740() {
        TestWork works[] = {
            TestWork.createManual("w1",null, Constraint.SNET, "01.01.08", null, null, "01.10.08", "01.10.08",  null),
            new TestWork("w2","w1", Constraint.ASAP, "01.01.08", 10, "01.10.08", "01.23.08",  null),
        };
        
        works[0].setActualStart("01.10.08");
        utils.run(works, false);

        Work work = works[0].getWork();
        if (work != null) {
            work.getSchedules().setPlannedLaborTime(Duration.getDaysDuration(5.f));
          //  work.save(user);
          //  work.setModifiedById(user.getId());
            WorkUtil.setScheduleDatesInternal(work);
        } else {
            works[0].getBean().setPlannedLaborTime(Duration.getDaysDuration(5.f));
        }
        
        utils.testWorks(works);
    }
    
    public void test54150() {
        utils.run(new TestWork[] {
            new TestWork("w1",null,     Constraint.ASAP, "02.01.08", null,  "02.01.08","02.18.2008",  null),
            new TestWork("w2","w1",     Constraint.ASAP,null, 10,    "02.01.08","02.14.2008",  null),
            new TestWork("w3","w1",     Constraint.ASAP,null, 1,     "02.15.08","02.18.2008",  "w2"),
            new TestWork("w4","w3",     Constraint.ASAP,null, 1,     "02.15.08","02.15.2008",  null),
            new TestWork("w5","w4",     Constraint.ASAP,null, 1,     "02.15.08","02.15.2008",  null),
            new TestWork("w6","w3",     Constraint.ASAP,null, 1,     "02.18.08","02.18.2008",  "w4"),
        });
    }
    
    
    public void test53436() {
        utils.run(new TestWork[] {
            new TestWork("w1",null,     Constraint.ASAP, "03.25.07",null,    "03.31.08","03.31.2008",  null),
            new TestWork("w2","w1",     Constraint.SNET,"03.30.08",null,     "03.31.08","03.31.2008",  null),
            new TestWork("w3","w2",     Constraint.ASAP,null, 1,             "03.31.08","03.31.2008",  null),
            new TestWork("w4","w3",     Constraint.ASAP,null, 1,             "03.31.08","03.31.2008",  null),
        });
        
        utils.run(new TestWork[] {
            new TestWork("w1",null,     Constraint.ASAP, "03.25.07",null,    "03.30.07","03.30.2007",  null),
            new TestWork("w2","w1",     Constraint.SNET,"03.30.07",null,     "03.30.07","03.30.2007",  null),
            new TestWork("w3","w2",     Constraint.ASAP,null, 1,             "03.30.07","03.30.2007",  null),
            new TestWork("w4","w3",     Constraint.ASAP,null, 1,             "03.30.07","03.30.2007",  null),
        });
    }
    
    public void testBug53086() {
        utils.run(new TestWork[] {
            new TestWork("w1",null,     Constraint.ASAP,null,null,  "12.03.07","12.14.2007",  null),
            new TestWork("w2","w1",     Constraint.ASAP,null, 5,    "12.03.07","12.07.2007",  null),
            new TestWork("w3","w1",     Constraint.ASAP,null, 5,    "12.10.07","12.14.2007",  "w2"),
        });
        
        // tests situation when one project dependent on another summary project.
        TestWork works[] = {
            new TestWork("w1",null,     Constraint.ASAP,null,null,  "01.10.08","01.22.08",  null),
            new TestWork("w2","w1",     Constraint.ASAP,null, 5,    "01.10.08","01.15.08",  null),
            new TestWork("w3","w1",     Constraint.ASAP,null, 5,    "01.16.08","01.22.08",  "w2"),
            
            new TestWork("w2-child","w2",     Constraint.ASAP,null, 3,    "01.10.08","01.15.08",  null),
        };
        
        works[3].setActualStart("01.10.08");
        works[3].setActualEnd("01.15.08");
        
        utils.run(works);
        
        works = new TestWork[] {
            new TestWork("w1",null,     Constraint.ASAP,"01.01.07",null,    "01.01.07","01.15.08",  null),
            new TestWork("w2","w1",     Constraint.ASAP,null, 6,            "01.01.07","01.08.07",  null),
            new TestWork("w3","w1",     Constraint.ASAP,null, 2,            "01.10.08","01.15.08",  "w2"),
            new TestWork("w3-child","w3",     Constraint.ASAP,null, 7,      "01.10.08","01.15.08",  null),
        };
        
        works[3].setActualStart("01.10.08");
        works[3].setActualEnd("01.15.08");
        
        utils.run(works);
        
        // could't move project start!
        new ThrowExceptionTest("w2", DependencyConstraintProblem.class, "w3") {
            void invoke() {
                TestWork [] works = {
                    new TestWork("w1",null,     Constraint.ASAP,null, null,    "01.10.08","01.15.08",  null),
                    new TestWork("w2","w1",     Constraint.ASAP,null, 6,            "01.01.07","01.08.07",  null),
                    new TestWork("w3","w1",     Constraint.ASAP,null, 2,            "01.10.08","01.15.08",  "w2"),
                    new TestWork("w3-child","w3",     Constraint.ASAP,null, 7,      "01.10.08","01.15.08",  null),
                };
                
                works[3].setActualStart("01.10.08");
                works[3].setActualEnd("01.15.08");
                
                utils.run(works);                
            }
        };
        
        works = new TestWork[]{
            new TestWork("w1",null,     Constraint.ASAP,null,null,  "01.01.08","01.17.08",  null),
            new TestWork("w2","w1",     Constraint.ASAP,null, 5,    "01.01.08","01.10.08",  null),
            new TestWork("w3","w1",     Constraint.ASAP,null, 5,    "01.11.08","01.17.08",  "w2"),
        };
        
        works[1].setActualStart("01.01.08");
        works[1].setActualEnd("01.10.08");
        
        utils.run(works);
        
        works = new TestWork[]{
            new TestWork("w1",null,     Constraint.ASAP,null,null,  "01.01.08","01.18.08",  null),
            new TestWork("w2","w1",     Constraint.ASAP,null, 5,    "01.01.08","01.12.08",  null),
            new TestWork("w3","w1",     Constraint.ASAP,null, 5,    "01.14.08","01.18.08",  "w2"),
        };
        
        works[1].setActualStart("01.01.08");
        works[1].setActualEnd("01.12.08"); // saterday
        
        utils.run(works);
        
        works = new TestWork[]{
            new TestWork("w1",null,     Constraint.ASAP,null,null,  "01.07.08","01.18.08",  null),
            new TestWork("w2","w1",     Constraint.ASAP,null, 5,    "01.07.08","01.12.08",  null),
            new TestWork("w3","w1",     Constraint.ASAP,null, 5,    "01.14.08","01.18.08",  "w2"),
        };
        
        works[1].setActualEnd("01.12.08"); // saterday
        
        utils.run(works);
        
        works = new TestWork[]{
            new TestWork("w1",null,     Constraint.ASAP,null,null,  "01.07.08","01.25.08",  null),
            new TestWork("w2","w1",     Constraint.ASAP,null, 5,    "01.07.08","01.12.08",  null),
            new TestWork("w3","w1",     Constraint.ASAP,null, 5,    "01.21.08","01.25.08",  "w2"),
        };
        
        works[1].setActualEnd("01.12.08"); // saterday
        works[2].setActualEnd("01.25.08");
        
        utils.run(works);
        
        works = new TestWork[]{
            new TestWork("w1",null,     Constraint.ASAP,null,null,  "01.01.08","01.15.08",  null),
            new TestWork("w2","w1",     Constraint.ASAP,null, 5,    "01.01.08","01.15.08",  null),
            TestWork.createFDWork("w3", "w2", Constraint.FD, "01.01.08", "01.12.08", 2, "01.01.08", "01.12.08", null),
        };
        
        works[1].setActualStart("01.01.08");
        works[1].setActualEnd("01.15.08"); 
        
        utils.run(works);
        
        works = new TestWork[]{
            new TestWork("w1",null,     Constraint.ASAP,null,null,  "01.01.08","01.25.08",  null),
            TestWork.createManual("w2", "w1", Constraint.ASAP,      "01.01.08", "01.10.08", 5, "01.01.08", "01.12.08", null),            
            new TestWork("w3","w1",     Constraint.ASAP,null, 5,    "01.21.08","01.25.08",  "w2"),
        };
        
        works[1].setActualEnd("01.12.08"); // saterday
        works[2].setActualEnd("01.25.08");
        
        utils.run(works);
        
        works = new TestWork[]{
            new TestWork("w1",null,     Constraint.ASAP,null,null,  "01.01.08","01.25.08",  null),
            TestWork.createManual("w2", "w1", Constraint.ASAP,      "01.01.08", "01.10.08", 5, "01.01.08", "01.12.08", null),            
            new TestWork("w3","w2",     Constraint.ASAP,null, 5,    "01.21.08","01.25.08",  null),
        };
        
        works[1].setActualEnd("01.12.08"); // saterday
        works[2].setActualEnd("01.25.08");
        
        utils.run(works);
        
        works = new TestWork[]{
            new TestWork("w1",null,     Constraint.ASAP,null,null,  "01.12.08","01.25.08",  null),
            new TestWork("w2","w1",     Constraint.ASAP,null, 7,    "01.12.08","01.12.08",  null),            
            new TestWork("w3","w2",     Constraint.ASAP,null, 5,    "01.21.08","01.25.08",  null),
        };
        
        works[1].setActualEnd("01.12.08");
        works[2].setActualEnd("01.25.08");
        
        utils.run(works);
    }

    public void testBug52900_2() {
        new ThrowExceptionTest("w4", DependencyConstraintProblem.class, "w5") {
            void invoke() {
                utils.run(new TestWork[] {
                        new TestWork("w1",null,     Constraint.ASAP,null,null,          "12.03.07","12.14.2007",  null),
                        new TestWork("w2","w1",     Constraint.SNET,"12.01.07", 3,     "12.03.07","12.12.2007",  null),
                        new TestWork("w3","w1",     Constraint.FNLT,"12.14.2007",3,    "12.12.07","12.14.2007",  "w2"),
                        
                        new TestWork("w4","w2",     Constraint.ASAP,null, 4,     "12.03.07","12.06.2007",  null),
                        new TestWork("w5","w2",     Constraint.ALAP,null, 4,     "12.07.07","12.12.2007",  "w4"),
                });
            }
        };
     
        utils.run(new TestWork[] {
                new TestWork("w1",null,     Constraint.ASAP,null,null,          "12.03.07","12.14.2007",  null),
                new TestWork("w2","w1",     Constraint.FNLT,"12.29.07",null,    "12.03.07","12.14.2007",  null),
                new TestWork("w3","w2",     Constraint.FNET,"12.14.2007",10,    "12.03.07","12.14.2007",  null),
        });
        
        
        utils.run(new TestWork[] {
                new TestWork("w1",null,     Constraint.ASAP,null,null,          "12.03.07","12.14.2007",  null),
                new TestWork("w2","w1",     Constraint.SNET,"11.01.07",null,    "12.03.07","12.14.2007",  null),
                new TestWork("w3","w2",     Constraint.FNET,"12.14.2007",10,    "12.03.07","12.14.2007",  null),
        });
        
        utils.run(new TestWork[] {
                new TestWork("w1",null,     Constraint.ASAP,null,null,          "12.03.07","12.14.2007",  null),
                new TestWork("w2","w1",     Constraint.SNET,"12.01.07",null,    "12.03.07","12.14.2007",  null),
                new TestWork("w3","w2",     Constraint.FNLT,"12.14.2007",10,    "12.03.07","12.14.2007",  null),
        });
        
        utils.run(new TestWork[] {
                new TestWork("w1",null,     Constraint.ASAP,null,null,          "11.01.07","11.14.2007",  null),
                new TestWork("w2","w1",     Constraint.SNET,"11.01.07",null,    "11.01.07","11.14.2007",  null),
                new TestWork("w3","w2",     Constraint.FNLT,"12.14.2007",10,    "11.01.07","11.14.2007",  null),
        });
        
        utils.run(new TestWork[] {
                new TestWork("w1",null,     Constraint.ASAP,"01.01.07",null,    "11.01.07","11.14.2007",  null),
                new TestWork("w2","w1",     Constraint.SNET,"11.01.07",null,    "11.01.07","11.14.2007",  null),
                new TestWork("w3","w2",     Constraint.FNLT,"12.14.2007",10,    "11.01.07","11.14.2007",  null),
        });
        
        utils.run(new TestWork[] {
                new TestWork("w1",null,     Constraint.ASAP,"01.01.09",null,    "11.01.07","11.14.2007",  null),
                new TestWork("w2","w1",     Constraint.SNET,"11.01.07",null,    "11.01.07","11.14.2007",  null),
                new TestWork("w3","w2",     Constraint.FNLT,"12.14.2007",10,    "11.01.07","11.14.2007",  null),
        });

        utils.run(new TestWork[] {
                new TestWork("w1",null,     Constraint.ASAP,null,null,    "11.01.07","11.14.2007",  null),
                new TestWork("w2","w1",     Constraint.SNET,"11.01.07",null,    "11.01.07","11.14.2007",  null),
                new TestWork("w3","w2",     Constraint.ALAP,"12.14.2007",10,    "11.01.07","11.14.2007",  null),
        });
        
        utils.run(new TestWork[] {
                new TestWork("w1",null,     Constraint.ASAP,"01.01.08",null,    "11.01.07","11.14.2007",  null),
                new TestWork("w2","w1",     Constraint.SNET,"11.01.07",null,    "11.01.07","11.14.2007",  null),
                new TestWork("w3","w2",     Constraint.ALAP,"12.14.2007",10,    "11.01.07","11.14.2007",  null),
        });
        
        utils.run(new TestWork[] {
                new TestWork("w1",null,     Constraint.ASAP,"01.01.07",null,    "11.01.07","11.14.2007",  null),
                new TestWork("w2","w1",     Constraint.SNET,"11.01.07",null,    "11.01.07","11.14.2007",  null),
                new TestWork("w3","w2",     Constraint.ALAP,"12.14.2007",10,    "11.01.07","11.14.2007",  null),
        });

        utils.run(new TestWork[] {
                new TestWork("w1",null,     Constraint.ASAP,null,null,    "11.15.07", "12.12.2007",  null),
                new TestWork("w2","w1",     Constraint.SNET,"11.15.07",null,    "11.15.07", "12.12.2007",  null),
                new TestWork("w3","w2",     Constraint.ASAP,"12.14.2007",10,    "11.15.07", "11.28.2007",  null),
                new TestWork("w4","w2",     Constraint.FNLT,"12.14.2007",10,    "11.29.07", "12.12.2007", "w3"),
        });
        
        utils.run(new TestWork[] {
                new TestWork("w1",null,     Constraint.ASAP,"01.01.07",null,    "11.15.07", "12.12.2007",  null),
                new TestWork("w2","w1",     Constraint.SNET,"11.15.07",null,    "11.15.07", "12.12.2007",  null),
                new TestWork("w3","w2",     Constraint.ASAP,"12.14.2007",10,    "11.15.07", "11.28.2007",  null),
                new TestWork("w4","w2",     Constraint.FNLT,"12.14.2007",10,    "11.29.07", "12.12.2007", "w3"),
        });
        
        utils.run(new TestWork[] {
                new TestWork("w1",null,     Constraint.ASAP,null,null,    "11.15.07", "12.12.2007",  null),
                new TestWork("w2","w1",     Constraint.SNET,"11.15.07",null,    "11.15.07", "12.12.2007",  null),
                new TestWork("w3","w2",     Constraint.ASAP,"12.14.2007",10,    "11.15.07", "11.28.2007",  null),
                new TestWork("w4","w2",     Constraint.FNLT,"12.14.2007",10,    "11.29.07", "12.12.2007", "w3"),
        });
        
        new ThrowExceptionTest("w3", DependencyConstraintProblem.class, "w4") {
            void invoke() {
                utils.run(new TestWork[] {
                        new TestWork("w1",null,     Constraint.ASAP,"01.01.07",null,    "11.20.07", "12.14.2007",  null),
                        new TestWork("w2","w1",     Constraint.SNET,"11.20.07",null,    "11.20.07", "12.14.2007",  null),
                        new TestWork("w3","w2",     Constraint.ASAP,"12.14.2007",10,    "11.20.07", "12.03.2007",  null),
                        new TestWork("w4","w2",     Constraint.FNLT,"12.14.2007",10,    "12.03.07", "12.14.2007", "w3"),
                });
            }
        };
    }
    
    public void testActionItems() {
        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.09.08",null),
            new TestWork(MasterTask.TYPE, "w2", "w1", Constraint.ALAP, null,   7, "01.01.08", "01.09.08",null, null),
            new TestWork(MasterTask.TYPE, "w3", "w1", Constraint.ALAP, null,   4, "01.04.08", "01.09.08", null, null),
        });

        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.09.08",null),
            new TestWork(MasterTask.TYPE, "w2", "w1", Constraint.ALAP, null,   7, "01.01.08", "01.09.08",null, null),
            new TestWork(MasterTask.TYPE, "w3", "w1", Constraint.ALAP, null,   4, "01.04.08", "01.09.08", "w2", "SS"),
        });

        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.09.08",null),
            new TestWork(MasterTask.TYPE, "w2", "w1", Constraint.ALAP, null,   7, "01.01.08", "01.09.08",null, null),
            new TestWork(MasterTask.TYPE, "w3", "w1", Constraint.ALAP, null,   4, "01.04.08", "01.09.08", "w2", "SF"),
        });

        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.15.08",null),
            new TestWork(MasterTask.TYPE, "w2", "w1", Constraint.ALAP, null,   7, "01.01.08", "01.09.08",null, null),
            new TestWork(MasterTask.TYPE, "w3", "w1", Constraint.ALAP, null,   4, "01.10.08", "01.15.08", "w2", "FS"),
        });

        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.09.08",null),
            new TestWork(MasterTask.TYPE, "w2", "w1", Constraint.ALAP, null,   7, "01.01.08", "01.09.08",null, null),
            new TestWork(MasterTask.TYPE, "w3", "w1", Constraint.ALAP, null,   4, "01.04.08", "01.09.08", "w2", "FF"),
        });
    }

    public void testCorrectDependencies() {
        String n1 = "Test_a2";
        utils.run(new TestWork[]{
                new TestWork(n1, null, Constraint.ASAP, "01.01.08",   3, "07.04.08", "07.31.08", null),
                new TestWork("w2", n1, Constraint.ASAP, null,         7, "07.04.08", "07.31.08", null),
                new TestWork("w3", n1, Constraint.ASAP, null,         7, "07.23.08", "07.31.08", "w2", "FS"),
                new TestWork("w5", n1, Constraint.ASAP, null,         7, "07.23.08", "07.31.08", "w2", "FS"),
            }, false);
    }

    public void testIncompatibleConstraints() {
        utils.setEnableConstraintTesting(true);

        new ThrowExceptionTest("w1", FNLTConstraintProblem.class) {
            void invoke() {
                utils.run(new TestWork[]{
                        new TestWork("w1", null, Constraint.FNLT, "01.01.08",20,"01.16.08","02.12.08", null),
                        new TestWork("w2", "w1", Constraint.SNET, "02.01.08", null, null, null, null),
                });
            }
        };

        new ThrowExceptionTest("w1", FNLTConstraintProblem.class) {
            void invoke() {
                utils.run(new TestWork[]{
                        new TestWork("w1", null, Constraint.FNLT, "01.05.08",20,"01.16.08","02.12.08", null),
                        new TestWork("w2", "w1", Constraint.SNET, "01.01.08", 20, null, null, null),
                });
            }
        };

        new ThrowExceptionTest("w2", DependencyConstraintProblem.class, "w3") {
            void invoke() {
                utils.run(new TestWork[]{
                        new TestWork("w1", null, Constraint.ASAP, "01.05.08",20,"01.16.08","02.12.08", null),
                        new TestWork("w2", "w1", Constraint.SNET, "01.01.08", 5, null, null, null),
                        new TestWork("w3", "w1", Constraint.FNLT, "01.05.08", 10, null, null, "w2"),
                });
            }
        };

        new ThrowExceptionTest("w3", DependencyConstraintProblem.class, "w4") {
            void invoke() {
                utils.run(new TestWork[]{
                        new TestWork("w1", null, Constraint.ASAP, "01.05.08",20,"01.16.08","02.12.08", null),
                        new TestWork("w2", "w1", Constraint.SNET, "01.01.08", 20, null, null, null),
                        new TestWork("w3", "w1", Constraint.ASAP, null, 5, null, null, "w2"),
                        new TestWork("w4", "w1", Constraint.FNLT, "01.15.08", 5, null, null, "w3"),
                });
            }
        };
    }

//    public void atestCreateWork() {
//        int size = 500;
//        TestWork works[] = new TestWork[size];
//
//        for (int i = 0; i < size; ++i) {
//            works[i] = new TestWork(String.valueOf(i), null, Constraint.ASAP, null,   5, "01.01.08", "01.09.08",null);
//        }
//
//        utils.run(works, false);
//        System.out.println("!!!!!!!!!");
//    }

    public void testLoopingDependency() {

        // should work
        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
            new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, null),
            new TestWork("w3", "w1", Constraint.ASAP, null, null, null, null, "w2"),
        }, false);
        
        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
            new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, null),
            new TestWork("w3", "w2", Constraint.ASAP, null, null, null, null, null),
        }, false);
        
        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
            new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, "w4"),
            new TestWork("w3", "w1", Constraint.ASAP, null, null, null, null, null),
            new TestWork("w4", "w3", Constraint.ASAP, null, null, null, null, null),
        }, false);
        
        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
            new TestWork("w3", "w1", Constraint.ASAP, null, null, null, null, null),
            new TestWork("w4", "w3", Constraint.ASAP, null, null, null, null, null),
            new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, "w4"),
        }, false);
        
        new ThrowExceptionTest("", CyclicDependencyProblem.class) {
            void invoke() {
                utils.run(new TestWork[]{
                    new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w3", "w2", Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w4", "w2", Constraint.ASAP, null, null, null, null, "w1"),
                }, false);
            }
        };
        
        new ThrowExceptionTest("", CyclicDependencyProblem.class) {
            void invoke() {
                utils.run(new TestWork[]{
                    new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w3", "w2", Constraint.ASAP, null, null, null, null, "w2"),
                }, false);
            }
        };

        new ThrowExceptionTest("w3 w2 w5 w4 w9 w8", CyclicDependencyProblem.class) {
            void invoke() {
                utils.run(new TestWork[]{
                        new TestWork("w0", null, Constraint.ASAP, null, null, null, null, null),
                        new TestWork("w1", "w0", Constraint.ASAP, null, 1, null, null, null),
                        new TestWork("w2", "w0", Constraint.ASAP, null, 1, null, null, null),
                        new TestWork("w3", "w1", Constraint.ASAP, null, 1, null, null, "w2"),
                        new TestWork("w4", "w1", Constraint.ASAP, null, 1, null, null, null),
                        new TestWork("w5", "w2", Constraint.ASAP, null, 1, null, null, "w4"),
                        new TestWork("w6", "w2", Constraint.ASAP, null, 1, null, null, "w5"),
                        new TestWork("w7", "w3", Constraint.ASAP, null, 1, null, null, "w6"),
                        new TestWork("w8", "w3", Constraint.ASAP, null, 1, null, null, null),
                        new TestWork("w9", "w4", Constraint.ASAP, null, 1, null, null, "w8"),
                    }, false);
            }
        };

        new ThrowExceptionTest("w2 w3", CyclicDependencyProblem.class) {
            void invoke() {
                  utils.run(new TestWork[]{
                      new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
                      new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, "w3"),
                      new TestWork("w3", "w1", Constraint.ASAP, null, null, null, null, "w2"),
                  });
            }
        };

        new ThrowExceptionTest("w2 w6 w5 w4 w3", CyclicDependencyProblem.class) {
            void invoke() {
                utils.run(new TestWork[]{
                    new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, "w6"),
                    new TestWork("w3", "w1", Constraint.ASAP, null, null, null, null, "w2"),
                    new TestWork("w4", "w1", Constraint.ASAP, null, null, null, null, "w3"),
                    new TestWork("w5", "w1", Constraint.ASAP, null, null, null, null, "w4"),
                    new TestWork("w6", "w1", Constraint.ASAP, null, null, null, null, "w5"),
                });
            }
        };

        new ThrowExceptionTest("", CyclicDependencyProblem.class) {
            void invoke() {
                utils.run(new TestWork[]{
                    new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, "w3"),
                    new TestWork("w3", "w2", Constraint.ASAP, null, null, null, null, null),
                });
            }
        };

        new ThrowExceptionTest("", CyclicDependencyProblem.class) {
            void invoke() {
                utils.run(new TestWork[]{
                    new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, "w6"),
                    new TestWork("w3", "w2", Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w4", "w3", Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w5", "w4", Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w6", "w5", Constraint.ASAP, null, null, null, null, null),
                });
            }
        };

        new ThrowExceptionTest("", CyclicDependencyProblem.class) {
            void invoke() {
                utils.run(new TestWork[]{
                    new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, "w4"),
                    new TestWork("w3", "w2", Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w4", "w2", Constraint.ASAP, null, null, null, null, "w3"),
                });
            }
        };

        new ThrowExceptionTest("", CyclicDependencyProblem.class) {
            void invoke() {
                utils.run(new TestWork[]{
                    new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, "w4"),
                    new TestWork("w3", "w2", Constraint.ASAP, null, null, null, null, "w2"),
                    new TestWork("w4", "w2", Constraint.ASAP, null, null, null, null, "w3"),
                });
            }
        };

        new ThrowExceptionTest("w2 w3 w4", CyclicDependencyProblem.class) {
            void invoke() {
                utils.run(new TestWork[]{
                    new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, "w3"),
                    new TestWork("w3", "w1", Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w4", "w3", Constraint.ASAP, null, null, null, null, "w2"),
                });
            }
        };

        new ThrowExceptionTest("w3 w6 w5 w4", CyclicDependencyProblem.class) {
            void invoke() {
                utils.run(new TestWork[]{
                    new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w3", "w1", Constraint.ASAP, null, null, null, null, "w6"),
                    new TestWork("w4", "w1", Constraint.ASAP, null, null, null, null, "w3"),
                    new TestWork("w5", "w1", Constraint.ASAP, null, null, null, null, "w4"),
                    new TestWork("w6", "w1", Constraint.ASAP, null, null, null, null, "w5"),
                });
            }
        };

        new ThrowExceptionTest("", CyclicDependencyProblem.class) {
            void invoke() {
                utils.run(new TestWork[]{
                    new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, "w6"),
                    new TestWork("w3", "w2", Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w4", "w3", Constraint.ASAP, null, null, null, null, "w3"),
                    new TestWork("w5", "w3", Constraint.ASAP, null, null, null, null, "w4"),
                    new TestWork("w6", "w3", Constraint.ASAP, null, null, null, null, "w5"),
                });
            }
        };

        new ThrowExceptionTest("w3", CyclicDependencyProblem.class) {
            void invoke() {
                utils.run(new TestWork[]{
                    new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w3", "w2", Constraint.ASAP, null, null, null, null, "w3"),
                    new TestWork("w4", "w3", Constraint.ASAP, null, null, null, null, "w3"),
                    new TestWork("w5", "w3", Constraint.ASAP, null, null, null, null, "w4"),
                    new TestWork("w6", "w3", Constraint.ASAP, null, null, null, null, "w5"),
                });
            }
        };

        new ThrowExceptionTest("w04 w08 w07 w11 w12 w10 w06 w05", CyclicDependencyProblem.class) {
            void invoke() {
                utils.run(new TestWork[]{
                    new TestWork("TestLooping", null, Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w02", "TestLooping", Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w03", "w02", Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w04", "w03", Constraint.ASAP, null, null, null, null, "w08"),
                    new TestWork("w05", "w04", Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w06", "w04", Constraint.ASAP, null, null, null, null, "w05"),
                    new TestWork("w07", "TestLooping", Constraint.ASAP, null, null, null, null, "w11"),
                    new TestWork("w08", "w07", Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w09", "w08", Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w10", "w04", Constraint.ASAP, null, null, null, null, "w06"),
                    new TestWork("w11", "TestLooping", Constraint.ASAP, null, null, null, null, null),
                    new TestWork("w12", "w11", Constraint.ASAP, null, null, null, null, "w10"),
                });
            }
        };

    }

    public void testIncorrectContraints() {
        new ThrowExceptionTest("w2 w3", CyclicDependencyProblem.class) {
            void invoke() {
                utils.run(new TestWork[]{
                        new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
                        new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, "w3"),
                        new TestWork("w3", "w1", Constraint.ASAP, null, null, null, null, "w2"),
                });
            }
        };
  
        // accordingly to MRD
       // utils.testConstraints(Constraint.ASAP, "Mile, Leaf, Sum, MSsum");
        //utils.testConstraints(Constraint.ALAP, "Mile, Leaf");
        utils.testConstraints(Constraint.MSO, "Leaf");
        utils.testConstraints(Constraint.MFO, "Mile, Leaf");
        utils.testConstraints(Constraint.SNET, "Leaf, Sum, MSsum");
        utils.testConstraints(Constraint.SNLT, "Leaf");
        utils.testConstraints(Constraint.FNET, "Mile, Leaf");
        utils.testConstraints(Constraint.FNLT, "Mile, Leaf, Sum, MSsum");
//        utils.testConstraints(Constraint.FD, "Leaf, MSsum");
    }

    public void atestBugs() {
        //issue 52287
        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.FNLT, "07.31.08",   3, "07.04.08", "07.31.08", null),
            new TestWork("w2", "w1", Constraint.ALAP, null,         20, "07.04.08", "07.31.08", null),
            new TestWork("w3", "w1", Constraint.ALAP, null,         7, "07.23.08", "07.31.08", null),
        });

        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.FNLT, "09.30.08",   3, "09.03.08", "09.30.08", null),
            new TestWork("w2", "w1", Constraint.ALAP, null,         20, "09.03.08", "09.30.08", null),
            new TestWork("w3", "w1", Constraint.ALAP, null,         7, "09.22.08", "09.30.08", null),
        });
    }

    public void testManualScheduling() {
        DateFormat df = new SimpleDateFormat ("MM.dd.yy");
        Timestamp curTime = utils.toWorkTime(utils.getCurTime());
        String today = df.format(curTime);
        Timestamp today_10 = utils.add(curTime, Duration.getDaysDuration(10.f));

        utils.run(new TestWork[]{
            TestWork.createManual("w1", null, Constraint.ASAP, null, null, null, today, today,null),
        });

        utils.run(new TestWork[]{
            TestWork.createManual("w1", null, Constraint.FD, null, null, 1, today, today,null),
        });

        utils.run(new TestWork[]{
            TestWork.createManual("w1", null, Constraint.ALAP, null, null, 10, today, df.format(today_10), null),
        });

        utils.run(new TestWork[]{
            TestWork.createManual("w1", null, Constraint.ALAP, "01.01.08", null, 2, "01.01.08", "01.02.08", null),
        });

        utils.run(new TestWork[]{
            TestWork.createManual("w1", null, Constraint.ALAP, null, "01.02.08", 2, "01.01.08", "01.02.08", null),
        });
        
        utils.run(new TestWork[]{
            TestWork.createManual("w1", null, Constraint.ASAP, "01.01.08", "03.02.08", 5, "01.01.08", "03.02.08", null),
            TestWork.createManual("w2", "w1", Constraint.FD, "01.01.08", "01.02.08", 7, "01.01.08", "01.02.08", null),
        });
        
        utils.run(new TestWork[]{
            TestWork.createManual("w1", null, Constraint.ASAP, "02.18.08", "03.02.08", 5, "02.18.08", "03.02.08", null),
            TestWork.createManual("w2", "w1", Constraint.ASAP, "02.18.08", "03.02.08", 10, "02.18.08", "03.02.08", null),
        });

        utils.run(new TestWork[]{
            TestWork.createManual("w1", null, Constraint.ASAP, "03.01.08", "03.02.08", 5, "03.01.08", "03.02.08", null),
            new TestWork("w2", "w1", Constraint.ASAP, null, 5, "03.03.08", "03.07.08", null),
        });
        
        utils.run(new TestWork[]{
            TestWork.createManual("w1", null, Constraint.ASAP, "03.02.08", "03.02.08", 5, "03.02.08", "03.02.08", null),
            // shifted to work days
            TestWork.createFDWork("w2", "w1", Constraint.FD, "03.05.08", "03.15.08", 2, "03.05.08", "03.15.08", null),
        });
        utils.run(new TestWork[]{
            TestWork.createManual("w1", null, Constraint.ASAP, "03.02.08", "03.02.08", 6, "03.02.08", "03.02.08", null),
            TestWork.createManual("w2", "w1", Constraint.ALAP, "03.04.08", "03.05.08", 2, "03.04.08", "03.05.08", null),
            TestWork.createFDWork("w3", "w1", Constraint.FD, "03.01.08", "03.10.08", 2, "03.01.08", "03.10.08", null),
        });
    }
    
    public void testManualScheduling2() {
        utils.run(new TestWork[]{
            TestWork.createManual("w1", null, Constraint.ASAP, null, "01.02.08", 2, "01.01.08", "01.02.08", null),
            TestWork.createManual("w2", "w1", Constraint.ASAP, null, "01.02.08", 2, "01.01.08", "01.02.08", null),
        });
    }

    public void testSimple() {
//        utils.run(new TestWork[]{
//            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.09.08",null),
//            new TestWork("w2", "w1", Constraint.ALAP, null,   7, "01.01.08", "01.09.08",null),
//            new TestWork("w3", "w1", Constraint.ALAP, null,   4, "01.04.08", "01.09.08", null),
//        });

        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.09.08",null),
            new TestWork("w2", "w1", Constraint.ALAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ALAP, null,   4, "01.04.08", "01.09.08", "w2", "SS"),
        });

        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.09.08",null),
            new TestWork("w2", "w1", Constraint.ALAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ALAP, null,   4, "01.04.08", "01.09.08", "w2", "SF"),
        });

        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.15.08",null),
            new TestWork("w2", "w1", Constraint.ALAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ALAP, null,   4, "01.10.08", "01.15.08", "w2", "FS"),
        });

        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.09.08",null),
            new TestWork("w2", "w1", Constraint.ALAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ALAP, null,   4, "01.04.08", "01.09.08", "w2", "FF"),
        });
    }

    public void testSimple2() {
        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.09.08",null),
            new TestWork("w2", "w1", Constraint.ALAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ASAP, null,   4, "01.01.08", "01.04.08", null),
        });

        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.09.08",null),
            new TestWork("w2", "w1", Constraint.ALAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ASAP, null,   4, "01.01.08", "01.04.08", "w2", "SS"),
        });

        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.09.08",null),
            new TestWork("w2", "w1", Constraint.ALAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ASAP, null,   4, "01.01.08", "01.04.08", "w2", "SF"),
        });

        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.15.08",null),
            new TestWork("w2", "w1", Constraint.ALAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ASAP, null,   4, "01.10.08", "01.15.08", "w2", "FS"),
        });

        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, null,   5, "01.01.08", "01.09.08",null),
            new TestWork("w2", "w1", Constraint.ALAP, null,   7, "01.01.08", "01.09.08",null),
            new TestWork("w3", "w1", Constraint.ASAP, null,   4, "01.04.08", "01.09.08", "w2", "FF"),
        });

        utils.run(new TestWork[]{
            new TestWork("w1", null, Constraint.ASAP, "01.01.08",   0, "01.01.08", "01.07.08",null),
            new TestWork("w2", "w1", Constraint.ALAP, "01.01.08",   5, "01.01.08", "01.07.08",null),
            new TestWork("w3", "w1", Constraint.ALAP, "01.01.08",   0, "01.07.08", "01.07.08", "w2", "FS"),
            new TestWork("w4", "w1", Constraint.ALAP, "01.01.08",   0, "01.07.08", "01.07.08", "w3", "FS"),
            new TestWork("w5", "w1", Constraint.ALAP, "01.01.08",   0, "01.07.08", "01.07.08", "w4", "FS"),
        });
    }

    public void testComplex() {
          utils.run(new TestWork[]{
              new TestWork("w11", null, Constraint.ASAP, null,   3, "11.28.07", "01.15.08",null),
              new TestWork("w2", "w11", Constraint.FNET, "01.15.08",   7, "01.07.08", "01.15.08", null),
              new TestWork("w3", "w11", Constraint.FNET, "12.10.07",   9, "11.28.07", "12.10.07", null),
          });

          utils.run(new TestWork[]{
              new TestWork("w12", null, Constraint.ASAP, null,   3, "11.28.07", "12.10.07",null),
              new TestWork("w2", "w12", Constraint.FNLT, "01.15.08",   7, "11.28.07", "12.06.07", null),
              new TestWork("w3", "w12", Constraint.FNLT, "12.10.07",   9, "11.28.07", "12.10.07", null),
          });

          utils.run(new TestWork[]{
              TestWork.createFDWork("w1", null, Constraint.FD, "01.10.08", "01.15.08", 7, "01.10.08", "01.15.08", null),
          });

          utils.run(new TestWork[]{
              new TestWork("w1", null, Constraint.MFO, "01.07.08", 5, "01.01.08", "01.07.08", null),
          });

          utils.run(new TestWork[]{
              new TestWork("w1", null, Constraint.MSO, "01.07.08", 5, "01.07.08", "01.11.08", null),
          });

          utils.run(new TestWork[]{
              new TestWork("w1", null, Constraint.ASAP, "01.21.08", 5, "01.07.08", "01.18.08", null),
              new TestWork("w2", "w1", Constraint.MFO, "01.12.08", 5, "01.07.08", "01.12.08", null),
              new TestWork("w3", "w1", Constraint.MSO, "01.12.08", 5, "01.12.08", "01.18.08", null),
          });
    }
    

    public void testSimple3() {
        
        utils.run( new TestWork[] {
                new TestWork("SNET",null,Constraint.SNET, "12.01.07",null,"12.03.07","02.22.2008",null),
                new TestWork("FNLT","SNET",Constraint.FNLT, "12.14.07",10,"12.03.07","12.14.2007",null),
                new TestWork("ASAP","SNET",Constraint.ASAP, null,50,"12.17.2007","02.22.08",2),
        });
        
        utils.run(new TestWork[]{
                new TestWork("Parent",null,     Constraint.ASAP,null,       null,   "02.01.08","02.14.08",null),
                new TestWork("SNET","Parent",   Constraint.SNET,"02.01.08", null,   "02.01.08","02.14.08",null),
                new TestWork("ASAP","SNET",     Constraint.ASAP,null,       10,     "02.01.2008","02.14.08",null),
        });
    }

    public void testMpp() {

        utils.setEnableConstraintTesting(false);
        TestWork[] projects = {
                new TestWork("s1", (String)null, Constraint.ASAP, null, 41,"01.01.08","02.26.08",null),
                new TestWork("s2", "s1", Constraint.ASAP, null, 41,"01.01.08","02.26.08",null),
                new TestWork("s3", "s2", Constraint.SNET,"01.01.08",41,"01.01.08","02.26.08",null),
                new TestWork("ASAP", "s3", Constraint.ASAP, null, 35,"01.01.08","02.18.08",null),
                new TestWork("Step 1", "ASAP", Constraint.ASAP, null, 5,"01.01.08","01.07.08",null),
                new TestWork("Step 2", "ASAP", Constraint.ASAP, null, 20,"01.08.08","02.04.08",5),
                new TestWork("Step 3", "ASAP", Constraint.ASAP, null, 10,"02.05.08","02.18.08",6),
                new TestWork("Done", "ASAP", Constraint.SNET,"02.18.08",0,"02.18.08","02.18.08",7),
                new TestWork("ALAP", "s3", Constraint.ASAP, null, 25,"01.23.08","02.26.08",null),
                new TestWork("Step 1", "ALAP", Constraint.ALAP, null, 5,"01.23.08","01.29.08",null),
                new TestWork("Step 2", "ALAP", Constraint.ALAP, null, 10,"01.30.08","02.12.08",10),
                new TestWork("Step 3", "ALAP", Constraint.ALAP, null, 10,"02.13.08","02.26.08",11),
                new TestWork("FNET", "s3", Constraint.ASAP, null, 41,"01.01.08","02.26.08",null),
                new TestWork("Step 1", "FNET", Constraint.ASAP, null, 5,"01.01.08","01.07.08",null),
                new TestWork("Step 2", "FNET", Constraint.FNET,"02.12.08",20,"01.16.08","02.12.08",14),
                new TestWork("Step 3", "FNET", Constraint.FNET,"02.21.08",10,"02.13.08","02.26.08",15),
                new TestWork("FNLT", "s3", Constraint.ASAP, null, 33,"01.01.08","02.14.08",null),
                new TestWork("Step 1", "FNLT", Constraint.ASAP, null, 5,"01.01.08","01.07.08",null),
                new TestWork("Step 2", "FNLT", Constraint.FNLT,"02.08.08",20,"01.08.08","02.04.08",18),
                new TestWork("Step 3", "FNLT", Constraint.FNLT,"02.14.08",10,"02.01.08","02.14.08",19),
                new TestWork("MFO", "s3", Constraint.ASAP, null, 35,"01.01.08","02.18.08",null),
                new TestWork("Step 1", "MFO", Constraint.ASAP, null, 5,"01.01.08","01.07.08",null),
                new TestWork("Step 2", "MFO", Constraint.MFO,"02.07.08",20,"01.11.08","02.07.08",22),
                new TestWork("Step 3", "MFO", Constraint.MFO,"02.18.08",10,"02.05.08","02.18.08",23),
                new TestWork("MSO", "s3", Constraint.ASAP, null, 34,"01.01.08","02.15.08",null),
                new TestWork("Step 1", "MSO", Constraint.ASAP, null, 5,"01.01.08","01.07.08",null),
                new TestWork("Step 2", "MSO", Constraint.MSO,"01.14.08",20,"01.14.08","02.08.08",26),
                new TestWork("Step 3", "MSO", Constraint.MSO,"02.03.08",10,"02.03.08","02.15.08",27),
                new TestWork("SNET", "s3", Constraint.ASAP, null, 40,"01.01.08","02.25.08",null),
                new TestWork("Step 1", "SNET", Constraint.ASAP, null, 5,"01.01.08","01.07.08",null),
                new TestWork("Step 2", "SNET", Constraint.SNET,"01.15.08",20,"01.15.08","02.11.08",30),
                new TestWork("Step 3", "SNET", Constraint.SNET,"01.29.08",10,"02.12.08","02.25.08",31),
                new TestWork("SNLT", "s3", Constraint.ASAP, null, 30,"01.01.08","02.11.08",null),
                new TestWork("Step 1", "SNLT", Constraint.ASAP, null, 5,"01.01.08","01.07.08",null),
                new TestWork("Step 2", "SNLT", Constraint.SNLT,"01.15.08",20,"01.08.08","02.04.08",34),
                new TestWork("Step 3", "SNLT", Constraint.SNLT,"01.29.08",10,"01.29.08","02.11.08",35),
                new TestWork("FF", "s3", Constraint.ASAP, null, 10,"01.01.08","01.14.08",null),
                new TestWork("Step 1", "FF", Constraint.ASAP, null, 5,"01.01.08","01.07.08",null),
                new TestWork("Step 2", "FF", Constraint.ASAP, null, 5,"01.08.08","01.14.08",38),
                new TestWork("Step 3", "FF", Constraint.ASAP, null, 5,"01.08.08","01.14.08",39,"FF"),
                new TestWork("SS", "s3", Constraint.ASAP, null, 10,"01.01.08","01.14.08",null),
                new TestWork("Step 1", "SS", Constraint.ASAP, null, 5,"01.01.08","01.07.08",null),
                new TestWork("Step 2", "SS", Constraint.ASAP, null, 5,"01.08.08","01.14.08",42),
                new TestWork("Step 3", "SS", Constraint.ASAP, null, 5,"01.08.08","01.14.08",43,"SS"),
                new TestWork("SF", "s3", Constraint.ASAP, null, 10,"01.01.08","01.14.08",null),
                new TestWork("Step 1", "SF", Constraint.ASAP, null, 5,"01.01.08","01.07.08",null),
                new TestWork("Step 2", "SF", Constraint.ASAP, null, 5,"01.08.08","01.14.08",46),

                // Microsoft project shows end as 08.01.08 but displays until 07.01.08!!!
                // duration from 01.01 to 07.01 is just a 5 days
                new TestWork("Step 3", "SF", Constraint.ASAP, null, 5,"01.01.08","01.07.08",47,"SF"),
                new TestWork("Late FF", "s3", Constraint.ASAP, null, 41,"01.01.08","02.26.08",null),
                new TestWork("Step 1", "Late FF", Constraint.ASAP, null, 5,"01.01.08","01.07.08",null),
                new TestWork("Step 2", "Late FF", Constraint.ALAP, null, 5,"02.20.08","02.26.08",50),
                new TestWork("Step 3", "Late FF", Constraint.ALAP, null, 5,"02.20.08","02.26.08",51,"FF"),
                new TestWork("Late SS", "s3", Constraint.ASAP, null, 41,"01.01.08","02.26.08",null),
                new TestWork("Step 1", "Late SS", Constraint.ASAP, null, 5,"01.01.08","01.07.08",null),
                new TestWork("Step 2", "Late SS", Constraint.ALAP, null, 5,"02.20.08","02.26.08",54),
                new TestWork("Step 3", "Late SS", Constraint.ALAP, null, 5,"02.20.08","02.26.08",55,"SS"),
                new TestWork("Late SF", "s3", Constraint.ASAP, null, 41,"01.01.08","02.26.08",null),
                new TestWork("Step 1", "Late SF", Constraint.ASAP, null, 5,"01.01.08","01.07.08",null),
                new TestWork("Step 2", "Late SF", Constraint.ALAP, null, 5,"02.20.08","02.26.08",58),
                new TestWork("Step 3", "Late SF", Constraint.ALAP, null, 5, "02.20.08","02.26.08",59,"SF")

        };

        utils.run(projects);
    } 
    
    public void testBug52900() {
        TestWork[] projects = {
                new TestWork("Big Widgets",null,                                                Constraint.ASAP,null,null,          "12.03.07","12.26.2008",  null),
                new TestWork("MP-074 - Widget V2.1","Big Widgets",                              Constraint.SNET,"12.01.07",null,    "12.03.07","05.16.2008",  null),
                new TestWork("MP-074 - Idea Screen & Selectio...","MP-074 - Widget V2.1",       Constraint.FNLT,"12.14.2007",10,    "12.03.07","12.14.2007",  null),
                new TestWork("MP-074 - Scoping","MP-074 - Widget V2.1",                         Constraint.ASAP,null,20,            "12.17.2007","01.11.08",  3),
                new TestWork("MP-074 - Build Business Case","MP-074 - Widget V2.1",             Constraint.ASAP,null,10,            "01.14.2008","01.25.2008",4),
                new TestWork("MP-074 - Development","MP-074 - Widget V2.1",                     Constraint.ASAP,null,50,            "01.28.2008","04.04.08",  5),
                new TestWork("MP-074 - Testing & Validation","MP-074 - Widget V2.1",            Constraint.ASAP,null,20,            "04.07.08","05.02.08",    6),
                new TestWork("MP-074 - Launch","MP-074 - Widget V2.1",                          Constraint.ASAP,null,10,            "05.05.08","05.16.2008",  7),
                new TestWork("MP-075 - Web enable widget","Big Widgets",                        Constraint.SNET,"02.01.08",null,    "02.01.08","07.17.2008",  null),
                new TestWork("MP-075 - Idea Screen & Selectio...","MP-075 - Web enable widget", Constraint.ASAP,null,10,            "02.01.08","02.14.2008",  null),
                new TestWork("MP-075 - Scoping","MP-075 - Web enable widget",                   Constraint.ASAP,null,20,            "02.15.2008","03.13.2008",10),
                new TestWork("MP-075 - Build Business Case","MP-075 - Web enable widget",       Constraint.ASAP,null,10,            "03.14.2008","03.27.2008",11),
                new TestWork("MP-075 - Development","MP-075 - Web enable widget",               Constraint.ASAP,null,50,            "03.28.2008","06.05.08",  12),
                new TestWork("MP-075 - Testing & Validation","MP-075 - Web enable widget",      Constraint.ASAP,null,20,            "06.06.08","07.03.08",    13),
                new TestWork("MP-075 - Launch","MP-075 - Web enable widget",                    Constraint.ASAP,null,10,            "07.04.08","07.17.2008",  14),
                new TestWork("MP-076 - Provide widget tracking","Big Widgets",                  Constraint.SNET,"01.01.08",null,    "01.01.08","06.02.08",    null),
                new TestWork("MP-076 - Idea Screen & Selectio...","MP-076 - Provide widget tracking",Constraint.ASAP,null,10,       "01.01.08","01.14.2008",  null),
                new TestWork("MP-076 - Scoping","MP-076 - Provide widget tracking",             Constraint.ASAP,null,20,            "01.15.2008","02.11.08",  17),
                new TestWork("MP-076 - Build Business Case","MP-076 - Provide widget tracking", Constraint.ASAP,null,10,            "02.12.08","02.25.2008",  18),
                new TestWork("MP-076 - Development","MP-076 - Provide widget tracking",         Constraint.ASAP,null,40,            "02.26.2008","04.21.2008",19),
                new TestWork("MP-076 - Testing & Validation","MP-076 - Provide widget tracking",Constraint.ASAP,null,20,            "04.22.2008","05.19.2008",20),
                new TestWork("MP-076 - Launch","MP-076 - Provide widget tracking",              Constraint.ASAP,null,10,            "05.20.2008","06.02.08",  21),
                new TestWork("MP-077 - Add GPS to widget","Big Widgets",                        Constraint.SNET,"03.04.08",null,    "03.04.08","09.15.2008",  null),
                new TestWork("MP-077 - Idea Screen & Selectio...","MP-077 - Add GPS to widget",Constraint.ASAP,null,10,             "03.04.08","03.17.2008",  null),
                new TestWork("MP-077 - Scoping","MP-077 - Add GPS to widget",                   Constraint.ASAP,null,20,            "03.18.2008","04.14.2008",24),
                new TestWork("MP-077 - Build Business Case","MP-077 - Add GPS to widget",       Constraint.ASAP,null,10,            "04.15.2008","04.28.2008",25),
                new TestWork("MP-077 - Development","MP-077 - Add GPS to widget",               Constraint.ASAP,null,70,            "04.29.2008","08.04.08",  26),
                new TestWork("MP-077 - Testing & Validation","MP-077 - Add GPS to widget",      Constraint.ASAP,null,20,            "08.05.08","09.01.08",    27),
                new TestWork("MP-077 - Launch","MP-077 - Add GPS to widget",                    Constraint.ASAP,null,10,            "09.02.08","09.15.2008",  28),
                new TestWork("MP-078 - Engineering Change 1024","Big Widgets",                  Constraint.SNET,"02.05.08",null,    "02.05.08","06.23.2008",  null),
                new TestWork("MP-078 - Idea Screen & Selectio...","MP-078 - Engineering Change 1024",Constraint.ASAP,null,10,       "02.05.08","02.18.2008",  null),
                new TestWork("MP-078 - Scoping","MP-078 - Engineering Change 1024",             Constraint.ASAP,null,20,            "02.19.2008","03.17.2008",31),
                new TestWork("MP-078 - Build Business Case","MP-078 - Engineering Change 1024", Constraint.ASAP,null,10,            "03.18.2008","03.31.2008",32),
                new TestWork("MP-078 - Development","MP-078 - Engineering Change 1024",         Constraint.ASAP,null,30,            "04.01.08","05.12.08",    33),
                new TestWork("MP-078 - Testing & Validation","MP-078 - Engineering Change 1024",Constraint.ASAP,null,20,            "05.13.2008","06.09.08",  34),
                new TestWork("MP-078 - Launch","MP-078 - Engineering Change 1024",              Constraint.ASAP,null,10,            "06.10.08","06.23.2008",  35),
                new TestWork("MP-079 - Widget 3.0","Big Widgets",                               Constraint.ASAP,null,null,          "05.19.2008","12.26.2008",null),
                new TestWork("MP-079 - Idea Screen & Selectio...","MP-079 - Widget 3.0",        Constraint.ASAP,null,10,            "05.19.2008","05.30.2008",8),
                new TestWork("MP-079 - Scoping","MP-079 - Widget 3.0",                          Constraint.ASAP,null,20,            "06.02.08","06.27.2008",  38),
                new TestWork("MP-079 - Build Business Case","MP-079 - Widget 3.0",              Constraint.ASAP,null,10,            "06.30.2008","07.11.08",  39),
                new TestWork("MP-079 - Development","MP-079 - Widget 3.0",                      Constraint.ASAP,null,90,            "07.14.2008","11.14.2008",40),
                new TestWork("MP-079 - Testing & Validation","MP-079 - Widget 3.0",             Constraint.ASAP,null,20,            "11.17.2008","12.12.08",  41),
                new TestWork("MP-079 - Launch","MP-079 - Widget 3.0",                           Constraint.ASAP,null,10,            "12.15.2008","12.26.2008", 42),
        };
        utils.run(projects);

    }
}
