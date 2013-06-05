@RestResource(urlMapping='/DataSummarizer/*')
global with sharing class DataSummarizerCaller{
    
 
  @HttpPost
    global static String doPost(String appName, String eventName, String eventField, DateTime startTime, DateTime endTime, Long timeInterval) {

        //Maps eventFields(row names) to the value in the codastjegga__ValueType__c row 
        Map<String,String> eventFieldMap=new map<String,String>{
            'codastjegga__CurrencyValue__c' =>'Currency',
            'codastjegga__FloatValue__c' =>'Float',
            'codastjegga__NumberValue__c' =>'Number',
            'codastjegga__LocaleValue__c' =>'Locale',
            'codastjegga__TextValue__c' =>'Text'
        };
        
    
    
        /*Default to empty in case of errors*/
        String summary='{}';
        String categorical='{}';
        
        
        try{
            //Long start = System.currentTimeMillis();
            DataSummarizer summarizer = new DataSummarizer();
            summarizer.summarize(appName, eventName ,eventFieldMap.get(eventField), startTime, endTime, timeInterval);
            
            //System.debug('DataSummarizer took' + (System.currentTimeMillis()-start) + 'ms');
            //System.debug('SIZES: '+summarizer.averages.size());
            Map<Long, Double> summarized= new Map<Long, Double>();
            if(summarizer.averages.size() ==summarizer.times.size()){
                Integer i=0;
                for(Long sumnmaryTime : summarizer.times){
                    Double average =summarizer.averages.get(i++);
                    //System.debug('TIME: '+sumnmaryTime+' i: '+i);
                    //System.debug('average '+ average +'  MYTIME: '+sumnmaryTime+' i: '+i);
                    summarized.put(sumnmaryTime, average);
                }
            }
            summary=JSON.serialize(summarized);
            //System.debug('Whole Summary Took ' + (System.currentTimeMillis()-start) + 'ms');
        }catch(Exception e){
            System.debug(e);
        }
        
        
        try{
            //Long start = System.currentTimeMillis();
            categorical = doCategorical(appName, eventName ,eventField, eventFieldMap.get(eventField),startTime,endTime);
            //System.debug('Categorical took ' + (System.currentTimeMillis()-start) + 'ms');
        }catch(Exception e){
            System.debug(e);
        }

       return '{"summarized":'+ summary +', "categorical":' +categorical+'}';
    }

    private static String doCategorical(String appName, String eventName, String rowName, String valueType, DateTime startTime, DateTime endTime){
        
        System.debug(startTime+' '+endTime);
        
        Map<String, Integer> result=new Map<String, Integer>();
       String query='SELECT '+ rowName +
                            ' FROM codastjegga__TrackedEvents__c'+
                            ' WHERE codastjegga__DatabaseName__c = :appName'+
                            ' AND codastjegga__EventName__c   = :eventName'+
                            ' AND codastjegga__ValueType__c   = :valueType'+
                            ' AND codastjegga__Timestamp__c > = :startTime'+
                            ' AND codastjegga__Timestamp__c < = :endTime';
                            
        List<SObject> dataEvents = Database.query(query);
                          
        Map<Object,Integer> maps=new Map<Object,Integer>();  
                         
        for(SObject ar: dataEvents ){
            Object val=ar.get(rowName);
              
            Integer c = maps.get(val);          
            
            if (c == null) {
                c=0;
            }
            maps.put(val,++c);
        }
        
        return JSON.serialize(maps);                               
    }
    
    
    
    

}