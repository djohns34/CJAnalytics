@isTest
private class DataSummarizerTestClass {
    
    static testMethod void summarizeNumbersTest() {

        //Basic event set
        List<codastjegga__TrackedEvents__c> insertEvents  = new List<codastjegga__TrackedEvents__c>();
        
        List<DateTime> numberValueTimes=new List<DateTime>{
            DateTime.newInstance( 99000L),
            DateTime.newInstance(100000L),
            DateTime.newInstance(199000L),
            DateTime.newInstance(200000L),
            DateTime.newInstance(299000L),
            DateTime.newInstance(300000L)
        };    
        List<Integer> numberValues=new List<Integer>{
            10,11,12,14,16,18
        };
        
        for(Integer i=0;i<numberValues.size();i++){
                    insertEvents.add(
            new codastjegga__TrackedEvents__c(
                 codastjegga__Timestamp__c = numberValueTimes.get(i),
                 codastjegga__ValueType__c = 'Number',
                 codastjegga__NumberValue__c = numberValues.get(i),
                 codastjegga__DatabaseName__c = 'TestDB',
                 codastjegga__Device_Id__c = 'Test',
                 codastjegga__EventName__c = 'Test'));
        }
        
        /*, same db, different event name*/
        for(Integer i=0;i<numberValues.size();i++){
                    insertEvents.add(
            new codastjegga__TrackedEvents__c(
                 codastjegga__Timestamp__c = numberValueTimes.get(i),
                 codastjegga__ValueType__c = 'Number',
                 codastjegga__NumberValue__c = numberValues.get(i),
                 codastjegga__DatabaseName__c = 'TestDB',
                 codastjegga__Device_Id__c = 'Test',
                 codastjegga__EventName__c = 'TestDifferent'));
        }



        /*, different db, same event name*/
                for(Integer i=0;i<numberValues.size();i++){
                    insertEvents.add(
            new codastjegga__TrackedEvents__c(
                 codastjegga__Timestamp__c = numberValueTimes.get(i),
                 codastjegga__ValueType__c = 'Number',
                 codastjegga__NumberValue__c = numberValues.get(i),
                 codastjegga__DatabaseName__c = 'TestDBNum2',
                 codastjegga__Device_Id__c = 'Test',
                 codastjegga__EventName__c = 'Test'));
        }
        
        insert insertEvents;

        //Start Testing
        Test.startTest();

        DataSummarizer summarizer = new DataSummarizer();

        System.debug('Summarize Numbers ');

        summarizer.summarize('TestDB', 'Test', 'Number', DateTime.newInstance(100000L), DateTime.newInstance(300000L), 100000L);
        System.assertEquals(100000L, summarizer.times.get(0));
        System.assertEquals(200000L, summarizer.times.get(1));
        System.assertEquals(2, summarizer.entries.get(0));
        System.assertEquals(2, summarizer.entries.get(1));
        System.assertEquals(11.5, summarizer.averages.get(0));
        System.assertEquals(15.0, summarizer.averages.get(1));
        
        summarizer.summarize('TestDB', 'TestDifferent', 'Number', DateTime.newInstance(100000L), DateTime.newInstance(300000L), 100000L);
        System.assertEquals(100000L, summarizer.times.get(0));
        System.assertEquals(200000L, summarizer.times.get(1));
        System.assertEquals(2, summarizer.entries.get(0));
        System.assertEquals(2, summarizer.entries.get(1));
        System.assertEquals(11.5, summarizer.averages.get(0));
        System.assertEquals(15.0, summarizer.averages.get(1));
        
        summarizer.summarize('TestDBNum2', 'Test', 'Number', DateTime.newInstance(100000L), DateTime.newInstance(300000L), 100000L);
        System.assertEquals(100000L, summarizer.times.get(0));
        System.assertEquals(200000L, summarizer.times.get(1));
        System.assertEquals(2, summarizer.entries.get(0));
        System.assertEquals(2, summarizer.entries.get(1));
        System.assertEquals(11.5, summarizer.averages.get(0));
        System.assertEquals(15.0, summarizer.averages.get(1));

        Test.stopTest();
    }
    
    static testMethod void summarizeFloatsTest() {

        //Basic event set
        List<codastjegga__TrackedEvents__c> insertEvents  = new List<codastjegga__TrackedEvents__c>();
        
        List<DateTime> floatTimes=new List<DateTime>{
            DateTime.newInstance( 99000L),
            DateTime.newInstance(100000L),
            DateTime.newInstance(199000L),
            DateTime.newInstance(200000L),
            DateTime.newInstance(299000L),
            DateTime.newInstance(300000L)
        };
        List<Double> floatValues=new List<Double>{
            10.5,11.5,12.5,14.5,16.5,18.5
        };
        
        for(Integer i=0;i<floatValues.size();i++){
                    insertEvents.add(
            new codastjegga__TrackedEvents__c(
                 codastjegga__Timestamp__c = floatTimes.get(i),
                 codastjegga__ValueType__c = 'Float',
                 codastjegga__FloatValue__c = floatValues.get(i),
                 codastjegga__DatabaseName__c = 'TestDB',
                 codastjegga__Device_Id__c = 'Test',
                 codastjegga__EventName__c = 'Test'));
        }
        
        /*, same db, different event name*/
        for(Integer i=0;i<floatValues.size();i++){
                    insertEvents.add(
            new codastjegga__TrackedEvents__c(
                 codastjegga__Timestamp__c = floatTimes.get(i),
                 codastjegga__ValueType__c = 'Float',
                 codastjegga__FloatValue__c = floatValues.get(i),
                 codastjegga__DatabaseName__c = 'TestDB',
                 codastjegga__Device_Id__c = 'Test',
                 codastjegga__EventName__c = 'TestDifferent'));
        }


        /*, different db, same event name*/
        for(Integer i=0;i<floatValues.size();i++){
            insertEvents.add(
            new codastjegga__TrackedEvents__c(
                 codastjegga__Timestamp__c = floatTimes.get(i),
                 codastjegga__ValueType__c = 'Float',
                 codastjegga__FloatValue__c = floatValues.get(i),
                 codastjegga__DatabaseName__c = 'TestDBNum2',
                 codastjegga__Device_Id__c = 'Test',
                 codastjegga__EventName__c = 'Test'));
        }
        
        insert insertEvents;

        //Start Testing
        Test.startTest();

        DataSummarizer summarizer = new DataSummarizer();

        System.debug('Summarize Floats ');

        summarizer.summarize('TestDB','Test','Float', DateTime.newInstance(100000L), DateTime.newInstance(300000L), 100000L);
        System.assertEquals(100000L, summarizer.times.get(0));
        System.assertEquals(200000L, summarizer.times.get(1));
        System.assertEquals(2, summarizer.entries.get(0));
        System.assertEquals(2, summarizer.entries.get(1));
        System.assertEquals(12.0, summarizer.averages.get(0));
        System.assertEquals(15.5, summarizer.averages.get(1));
        
        summarizer.summarize('TestDB','TestDifferent','Float', DateTime.newInstance(100000L), DateTime.newInstance(300000L), 100000L);
        System.assertEquals(100000L, summarizer.times.get(0));
        System.assertEquals(200000L, summarizer.times.get(1));
        System.assertEquals(2, summarizer.entries.get(0));
        System.assertEquals(2, summarizer.entries.get(1));
        System.assertEquals(12.0, summarizer.averages.get(0));
        System.assertEquals(15.5, summarizer.averages.get(1));
        
        summarizer.summarize('TestDBNum2','Test','Float', DateTime.newInstance(100000L), DateTime.newInstance(300000L), 100000L);
        System.assertEquals(100000L, summarizer.times.get(0));
        System.assertEquals(200000L, summarizer.times.get(1));
        System.assertEquals(2, summarizer.entries.get(0));
        System.assertEquals(2, summarizer.entries.get(1));
        System.assertEquals(12.0, summarizer.averages.get(0));
        System.assertEquals(15.5, summarizer.averages.get(1));

        Test.stopTest();
    }
    
    
    static testMethod void summarizeCurrenciesTest() {

        //Basic event set
        List<codastjegga__TrackedEvents__c> insertEvents  = new List<codastjegga__TrackedEvents__c>();
        
        List<DateTime> currencyTimes=new List<DateTime>{
            DateTime.newInstance( 99000L),
            DateTime.newInstance(100000L),
            DateTime.newInstance(199000L),
            DateTime.newInstance(200000L),
            DateTime.newInstance(299000L),
            DateTime.newInstance(300000L)
        };
        List<Double> currencyValues=new List<Double>{
            10.5,11.5,12.5,14.5,16.5,18.5
        };
        
        for(Integer i=0;i<currencyValues.size();i++){
                    insertEvents.add(
            new codastjegga__TrackedEvents__c(
                 codastjegga__Timestamp__c = currencyTimes.get(i),
                 codastjegga__ValueType__c = 'Currency',
                 codastjegga__CurrencyValue__c= currencyValues.get(i),
                 codastjegga__DatabaseName__c = 'TestDB',
                 codastjegga__Device_Id__c = 'Test',
                 codastjegga__EventName__c = 'Test'));
        }
        
         /*, same db, different event name*/
        for(Integer i=0;i<currencyValues.size();i++){
                    insertEvents.add(
            new codastjegga__TrackedEvents__c(
                 codastjegga__Timestamp__c = currencyTimes.get(i),
                 codastjegga__ValueType__c = 'Currency',
                 codastjegga__CurrencyValue__c= currencyValues.get(i),
                 codastjegga__DatabaseName__c = 'TestDB',
                 codastjegga__Device_Id__c = 'Test',
                 codastjegga__EventName__c = 'TestDifferent'));
        }

        /*, different db, same event name*/
        for(Integer i=0;i<currencyValues.size();i++){
                    insertEvents.add(
            new codastjegga__TrackedEvents__c(
                 codastjegga__Timestamp__c = currencyTimes.get(i),
                 codastjegga__ValueType__c = 'Currency',
                 codastjegga__CurrencyValue__c= currencyValues.get(i),
                 codastjegga__DatabaseName__c = 'TestDBNum2',
                 codastjegga__Device_Id__c = 'Test',
                 codastjegga__EventName__c = 'Test'));
        }        
        
        insert insertEvents;

        //Start Testing
        Test.startTest();

        DataSummarizer summarizer = new DataSummarizer();

        System.debug('Summarize Currency ');

        summarizer.summarize('TestDB','Test','Currency', DateTime.newInstance(100000L), DateTime.newInstance(300000L), 100000L);
        System.assertEquals(100000L, summarizer.times.get(0));
        System.assertEquals(200000L, summarizer.times.get(1));
        System.assertEquals(2, summarizer.entries.get(0));
        System.assertEquals(2, summarizer.entries.get(1));
        System.assertEquals(12.0, summarizer.averages.get(0));
        System.assertEquals(15.5, summarizer.averages.get(1));

        Test.stopTest();
    }
    
    static testMethod void badValueTypeTest() {
        insert new codastjegga__TrackedEvents__c(
                    codastjegga__Timestamp__c = DateTime.newInstance(1000),
                    codastjegga__ValueType__c = 'Number',
                    codastjegga__NumberValue__c= 0,
                    codastjegga__DatabaseName__c = 'TestDB',
                    codastjegga__Device_Id__c = 'Test',
                    codastjegga__EventName__c = 'Test');
                    
        Test.startTest();
        
        boolean exceptionCaught = false;
        DataSummarizer summarizer = new DataSummarizer();
        
        try {
            //valueType parameter is intentionally misspelled to test the bad input response
            summarizer.summarize('TestDB','Test','Numer', DateTime.newInstance(0000L), DateTime.newInstance(2000L), 1000L);
        }
        catch (Exception e) {
            exceptionCaught = true;
        }
        
        System.assertEquals(true, exceptionCaught);
    }
}