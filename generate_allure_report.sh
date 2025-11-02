#!/bin/bash
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
cd '/home/runner/.m2/repository/allure/allure-2.35.1/bin/'
bash allure serve $parent_path'/allure-results' -h localhost
exit