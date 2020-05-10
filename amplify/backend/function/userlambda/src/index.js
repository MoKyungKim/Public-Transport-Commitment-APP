/* Amplify Params - DO NOT EDIT
	ENV
	REGION
	STORAGE_USER_ARN
	STORAGE_USER_NAME
Amplify Params - DO NOT EDIT */

var AWS = require('aws-sdk');
var region = process.env.REGION
var storageUserName = process.env.STORAGE_USER_NAME
AWS.config.update({region: region});
var ddb = new AWS.DynamoDB({apiVersion: '2012-08-10'});
var ddb_table_name = storageUserName
var ddb_primary_key = 'todo';

function write(params, context){
    ddb.putItem(params, function(err, data) {
    if (err) {
      console.log("Error", err);
    } else {
      console.log("Success", data);
    }
  });
}
exports.handler = async (event) => {
    // TODO implement

  var params = {
    TableName: storageUserName,
    Item: AWS.DynamoDB.Converter.input(event.arguments)
  };

  console.log('len: ' + Object.keys(event).length)
  if (Object.keys(event).length > 0) {
    write(params, context);
  }


};
