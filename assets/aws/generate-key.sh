aws ec2 create-key-pair \
--key-name eksKeyPair \
--query 'KeyMaterial' \
--output text > eksKeyPair.pem

aws ec2 describe-key-pairs \
--key-name eksKeyPair

chmod 400 ./eksKeyPair.pem
ssh-keygen -y -f ./eksKeyPair.pem > my-public-key.pub