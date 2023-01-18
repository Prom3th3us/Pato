sbt clean
rm -rf target
rm -rf pcs/target
rm -rf readside/target
rm -rf common/target
rm -rf project/target
rm -rf project/project/target
rm -rf it/target
cd ..
zip -r pcs.zip pcs
