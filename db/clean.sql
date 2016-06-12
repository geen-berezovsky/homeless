-- preparing clean dump

use homeless;

delete from BasicDocumentRegistry;
delete from ContractControl;
delete from CustomDocumentRegistry;
delete from Document;
delete from DocumentScan;
delete from link_breadwinner_client;
delete from link_chronicdisease_client;
delete from link_reasonofhomeless_client;
delete from RecievedService;
delete from ShelterHistory;
delete from ZAGSRequestDocumentRegistry;
delete from ServContract;
delete from Client;

update Worker set password='6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2';
