package ps5.support.grid.model2;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import ps5.psapi.WorkHelp;
import ps5.psapi.person.PortfolioHelp;
import ps5.support.grid.Controls;
import ps5.support.grid.ControlConf.StatusControlConfig;
import ps5.wbs.logic.validation.DataHandlingError;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.currency.Currency;
import com.cinteractive.ps3.currency.Money;
import com.cinteractive.ps3.documents.Document;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.portfolio.Portfolio;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkStatus;

enum WorkProperties {
    NAME,
    STATUS,
    PERCENT_COMPLETE
}


class PortfolioDataSource implements IDataSource<Work> {

    InstallationContext context;
    User user;
    
    Work root;
    Map<String, Work> projects = new HashMap<String, Work>();
    
    public PortfolioDataSource(String name, User user, InstallationContext context) {
        this.context = context;
        this.user = user;
        
        root = Work.createNew(Work.TYPE, name, user);
    }
    
    public PortfolioDataSource(PersistentKey portfolioId, User user, InstallationContext context) {
        this.context = context;
        this.user = user;
        
        Portfolio portfolio = Portfolio.get(portfolioId, context);
        root = Work.createNew(Work.TYPE, portfolio.getName(), user);
        List<Work> projects = PortfolioHelp.getProjectListByPortfolio(portfolio, user);
        for (Work work : projects) {
            this.addWork(work);
        }
    }
    
    public void addWork(Work work) {
        projects.put(String.valueOf( work.getId() ), work);
    }
    
    @Override
    public boolean applyFiltering(Filter<Work> filter) {
        return false;
    }

    @Override
    public boolean applySorting(Comparator<Work> sorter) {
        return false;
    }

    @Override
    public Work getById(String id) {
        if (id == null) {
            return root;
        }
        return projects.get(id);
    }

    @Override
    public int getChildCount(String parentId) {
        return projects.size();
    }

    @Override
    public List<String> getChildIds(String parentId) {
        return new ArrayList<String>( projects.keySet() );
    }
    
}

/*
class DashboardGridDataModel extends GridDataModel<Work> {

    private Portfolio portfolio;
    private User user;

    private DashboardGridDataModel(Portfolio portfolio, User user) {
        this.portfolio = portfolio;
        this.user = user;
    }
    
    private List<Work> data;
    private Map<PersistentKey, Work> dataById;
    
    @Override
    public void load() {
        this.data = PortfolioHelp.getProjectListByPortfolio(portfolio, user);
        for (Work work : data) {
            this.dataById.put(work.getId(), work);
        }
    }
    
    @Override
    public Work getRoot() {
        return null;    // flat list
    }
    @Override
    public Work getChild(Work parent, int index) {
        return this.data.get(index);
    }
    @Override
    public int getChildrenCount(Work parent) {
        return this.data.size();
    }
}
*/

class DashboardGridColumnModel extends ColumnModel<Work> {
    
    public static final IColumnDefinition<Work, String> PROJECT_NAME = 
            new ColumnDefinition<Work, String>("name", String.class) 
    {
        @Override
        public String getValue(Work obj) {
            return obj.getName();
        }
        @Override
        public void setValue(Work obj, String value) {
            obj.setName(value);
        }
    };
    
    public static final IColumnDefinition<Work, WorkStatus> STATUS 
            = new ColumnDefinition<Work, WorkStatus>("status", WorkStatus.class) 
    {
        {
//            dependsOn(WorkProperties.STATUS);
//            affects(WorkProperties.STATUS, WorkProperties.PERCENT_COMPLETE);
        }
        
        @Override
        public WorkStatus getValue(Work obj) {
            return obj.getStatus();
        }
        @Override
        public void setValue(Work obj, WorkStatus status) {
            obj.setStatus(status);
            //super.setModified(obj, this, WorkProperties.STATUS);
            if (WorkStatus.COMPLETED .equals( status )) {
                obj.setPercentComplete(100);
                //super.setModified(obj, this, WorkProperties.PERCENT_COMPLETE);
            }
            
        }
        
        @Override
        public String toDisplayString(WorkStatus status) {
            return "<img src='" + status.getTypeIconSrc() + "'/>&nbsp;" 
                    + status.getLocalizedName( super.getPrincipal().getLocale(), super.getContext() );
        }
        
        @Override
        public WorkStatus fromString(String value) {
            return WorkStatus.get(value);
        }

        @Override
        public String toEditString(WorkStatus status) {
            return status.getCode();
        }
//        @Override
//        public JSONObject getControl(User user) {
//            return Controls.getStatus(new StatusControlConfig("status", user));
//        }
        
        @Override
        public Comparator<Work> getComparator() {
            return new WorkHelp.CompStatus();
        }
    };
    
    
    public static final IColumnDefinition<Work, Integer> PERCENT_COMPLETE 
            = new ColumnDefinition<Work, Integer>("percent_complete", Integer.class) 
    {

        @Override
        public Integer getValue(Work obj) {
            return obj.getPercentComplete();
        }

        @Override
        public void setValue(Work obj, Integer value) {
            obj.setPercentComplete(value);
        }

    };
    
    public static final IColumnDefinition<Work, Integer> PRIORITY 
	    = new ColumnDefinition<Work, Integer>("priority", Integer.class) 
	{
	
		@Override
		public Integer getValue(Work obj) {
		    return obj.getAssignedPriority();
		}
		
		@Override
		public void setValue(Work obj, Integer value) {
		    obj.setAssignedPriority(value);
		}
	
	};
    
    public static final IColumnDefinition<Work, Integer> DOCUMENT_COUNT 
            = new ColumnDefinition<Work, Integer>("document_count", Integer.class) 
    {
        {
            super.addSummaryFunc("sum", new Sum());
            super.addSummaryFunc("avg", new Avg());
        }
        
        @Override
        public boolean isEditableColumn() {
            return false;
        }

        
        @Override
        public Integer getValue(Work obj) {
            List<Document> docs = obj.listDocuments(null, null, false);
            return docs == null ?  0 : docs.size();
        }

        @Override
        public void setValue(Work obj, Integer value) {
            throw new UnsupportedOperationException("document_count is read-only");
        }
        
    };
    
    public static final IColumnDefinition<Work, Money> BUDGET 
            = new ColumnDefinition<Work, Money>("budget", Money.class) 
    {
        public final String DEFAULT_CURRENCY_PATTERN = "#,##0.00";
        
        private Currency getTargetCurrency() {
            return (Currency) super.getEnvironmentProperty("dashboard.currency");
        }
        
        private Money getMoneyInTargetCurrency(Money value) {
            return value.convertToCurrency(super.getContext(), this.getTargetCurrency(), null);
        }
        
        // Money must be presented in the user-selected Dashboard currency.
        // this column must get this currency from somewhere...
        @Override
        public Money getValue(Work obj) {
            return obj.getBudgetedCostMoney( obj.getCurrency() );
        }

        @Override
        public void setValue(Work obj, Money value) {
            Money valueInWorkCurrency = value.convertToCurrency(super.getContext(), obj.getCurrency(), new Date()); 
            obj.setBudgetedCostAmount( valueInWorkCurrency.getAmount() );
        }

        
        @Override
        public String toDisplayString(Money value) {
            return this.getMoneyInTargetCurrency(value).toFormattedString();
        }
        
        
        {
            /*
            super.addSummaryFunc("sum", new SummaryFunc<Money>() {

                @Override
                public Money compute(Iterator<Money> valueIt) {
                    double sum = 0.0;
                    while (valueIt.hasNext()) {
                        Money valueInTargetCurrency = getMoneyInTargetCurrency( valueIt.next() );
                        sum += valueInTargetCurrency.getAmount();
                    }
                    return Money.getMoney(sum, getTargetCurrency());
                }
                
            });
            */
        }

    };
    
    public DashboardGridColumnModel(GridEnvironment env) {
        super(env);
        super.addColumn(PROJECT_NAME);
        super.addColumn(STATUS);
        super.addColumn(DOCUMENT_COUNT);
        super.addColumn(PRIORITY);
    }
    
    @Override
    public void load() {
        // load layout and user's last settings
    }
    
    @Override
    public void save() {
        // save layout and user's current settings
    }

	@Override
	public String getMasterColumnId() {
		return PROJECT_NAME.getId();
	}
}

