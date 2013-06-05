@isTest
private class DataSummarizerCallerTest{
   class ApexResponse{
    
    Map<String,Double> summarized;
    Map<String,Integer> categorical;
    
  }

  static testMethod void textTest() {
        //Basic event set
        List<codastjegga__TrackedEvents__c> insertEvents  = new List<codastjegga__TrackedEvents__c>();
        
        List<Integer> textValueCounts=new List<Integer>{
            3,7
        };
        
        for(Integer i=0;i<textValueCounts.size();i++){
            Integer value=textValueCounts.get(i);
            for(Integer j=0;j<value;j++){
                        insertEvents.add(
                new codastjegga__TrackedEvents__c(
                     codastjegga__Timestamp__c = DateTime.newInstance(1000L),
                     codastjegga__ValueType__c = 'Text',
                     codastjegga__TextValue__c = 'Text'+value,
                     codastjegga__DatabaseName__c = 'TestDB',
                     codastjegga__Device_Id__c = 'Test',
                     codastjegga__EventName__c = 'Test'));
            }
        }
        
        insert insertEvents;

        //Start Testing
        Test.startTest();
        
              Schema.DescribeFieldResult example = codastjegga__TrackedEvents__c.codastjegga__TextValue__c.getDescribe();
            System.debug('Can CustomField__c be groupable?'+example.groupable);

        String result = DataSummarizerCaller.doPost('TestDB','Test','codastjegga__TextValue__c',Datetime.newInstance(0L),Datetime.now(),1);

        System.assertNotEquals('',result);
        
        ApexResponse response = (ApexResponse)JSON.deserializeStrict(result,ApexResponse.class);
        System.debug('RESOBJ '+response);
        
        System.assertEquals(2,response.categorical.size());
        System.assertEquals(3,response.categorical.get('Text3'));
        System.assertEquals(7,response.categorical.get('Text7'));
        Test.stopTest();
    }
    
    static testMethod void numberTest() {
        //Basic event set
        List<codastjegga__TrackedEvents__c> insertEvents  = new List<codastjegga__TrackedEvents__c>();
        
        List<Integer> numberValueCounts=new List<Integer>{
            3,7
        };
        
        for(Integer i=0;i<numberValueCounts.size();i++){
            Integer value=numberValueCounts.get(i);
            for(Integer j=0;j<value;j++){
                        insertEvents.add(
                new codastjegga__TrackedEvents__c(
                     codastjegga__Timestamp__c = DateTime.newInstance(1000L),
                     codastjegga__ValueType__c = 'Number',
                     codastjegga__NumberValue__c = value,
                     codastjegga__DatabaseName__c = 'TestDB1',
                     codastjegga__Device_Id__c = 'Test',
                     codastjegga__EventName__c = 'Test'));
            }
        }
        
        insert insertEvents;

        //Start Testing
        Test.startTest();
        
        
        String result = DataSummarizerCaller.doPost('TestDB1','Test','codastjegga__NumberValue__c',Datetime.newInstance(0L),Datetime.newInstance(86400l*2L),86400l);/*Two days, summarized Daily*/

        System.assertNotEquals('',result);
        
        ApexResponse response = (ApexResponse)JSON.deserializeStrict(result,ApexResponse.class);
        System.debug('RESOBJ '+response);
        
        System.assertEquals(2,response.categorical.size());
        System.assertEquals(3,response.categorical.get('3'));
        System.assertEquals(7,response.categorical.get('7'));
        
        System.assertEquals(2,response.categorical.size());
        System.assert(response.summarized.containsKey('0'));
        System.assert(response.summarized.containsKey('86400'));
                                                              /*Expected value*/
        Double correct=Math.abs(response.summarized.get('0')-(((3*3)+(7*7))/10.0));                               
        System.assert(correct < 0.0001);
        System.assertEquals(0,response.summarized.get('86400'));
        
        Test.stopTest();
    }

}