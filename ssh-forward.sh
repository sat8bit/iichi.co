#!/bin/bash

ssh -L 9990:localhost:9990 -L 3306:localhost:3306 iichi.co
