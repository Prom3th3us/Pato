rm  -rf target
rm -rf pcs/target
rm -rf readside/target
rm -rf common/target
rm -rf project/target
rm -rf project/project/target
rm -rf it/target
mv .git ../.git
cd ..
rm pcs.zip
zip -r pcs.zip pcs
cd -
mv ../.git .git
