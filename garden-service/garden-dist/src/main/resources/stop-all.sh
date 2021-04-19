#!/bin/sh

jps |egrep 'garden-auth|garden-sys|garden-zuul'|awk '{print $1}'|xargs -t -i kill -9 {}
echo "stopped all services!"
